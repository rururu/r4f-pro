(ns algo.exe
(:use protege.core)
(:require
  [rete.core :as rete])
(:import
  edu.stanford.smi.protege.model.Instance))

(def do-next nil)
(def TRACE nil)
(defn uncomment [src]
  (rete.core/slurp-with-comments (java.io.StringReader. src)))

(defn ob-to-code [ob]
  `(.getInstance *kb*  ~(.getName ob)))

(defn trans-obs [obs]
  (if (= (count obs) 1)
  (ob-to-code (first obs))
  (vec (map ob-to-code obs))))

(defn var-val-map [bnd]
  (let [p2 (partition 2 bnd)
      vars (map first p2)
      nams (map name vars)]
  (zipmap nams vars)))

(defn val-to-code [val]
  (cond
  (vector? val) (vec (map val-to-code val))
  (seq? val) (map val-to-code val)
  (instance? Instance val) (ob-to-code val)
  true val))

(defn to-bnd [vvm]
  (vec (reduce-kv #(concat %1[(symbol %2) (val-to-code %3)]) [] vvm)))

(defn trace [bool]
  (def TRACE bool))

(defn do-trace [inst bnd]
  (when TRACE
  (println :binding bnd)
  (println (typ inst) (sv inst "title"))))

(defn do-input [inp bnd]
  (do-trace inp bnd)
(do-next 
  (sv inp "next")
  (let [code (sv inp "code")
          vprs (map #(list (symbol (first %)) (second %))
	(partition 2 (read-string (str "[" (uncomment  code) "]"))))
          oprs (map #(list (symbol (sv % "variable")) (trans-obs (svs % "objects")))
	(svs inp "object-rows"))
          uprs (filter #(not (some #{(first %)} bnd))
	(concat vprs oprs))
          ubnd (apply concat uprs)]
    (concat bnd ubnd))))

(defn do-code [pord bnd]
  (let [code (sv pord "code")
       bnd2 (read-string (str "[" (uncomment  code) "]"))
       bnd3 (vec (concat bnd bnd2))
       vvm1 (var-val-map bnd3)
       expr `(let ~bnd3 ~vvm1)
       ;;_ (println :expr expr)
       vvm2  (eval expr)]
  (to-bnd vvm2)))

(defn do-process [proc bnd]
  (do-trace proc bnd)
(do-next (sv proc "next") (do-code proc bnd)))

(defn do-variant [vrt vrts bnd]
  (if (<= 1 vrt (count vrts))
  (do-next (nth vrts (dec vrt)) bnd)))

(defn do-decision [dec bnd]
  (do-trace dec bnd)
(let [bnd2 (do-code dec bnd)]
  (do-variant (last bnd2) 
	(vec (svs dec "variants")) 
	(vec (butlast (butlast bnd2))))))

(defn do-preproc [prep bnd]
  (do-trace prep bnd)
(do-next (sv prep "next") (do-algorithm (sv prep "algorithm") bnd)))

(defn do-algorithm [algo bnd]
  (do-trace algo bnd)
(do-next (sv algo "begin") bnd))

(defn do-next [inst bnd]
  (if (some? inst)
  (condp = (typ inst)
    "Process" (do-process inst bnd)
    "Decision" (do-decision inst bnd)
    "PredefinedProcess" (do-preproc inst bnd)
    "Input" (do-input inst bnd)
    (println (str "Unknown type: " typ)))
  bnd))

