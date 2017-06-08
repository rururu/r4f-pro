(ns algo.exe
(:use protege.core)
(:require
  [rete.core :as rete]))

(def do-next nil)
(def TRACE nil)
(defn uncomment [src]
  (rete.core/slurp-with-comments (java.io.StringReader. src)))

(defn val-from-str [s]
  (try
  (Integer/parseInt s)
  (catch NumberFormatException e
    (try
      (Double/parseDouble s)
      (catch NumberFormatException e
        s)))))

(defn trans-obs [obs]
  (if (= (count obs) 1)
  (first obs)
  (vec obs)))

(defn trans-vals [vals]
  (trans-obs (map val-from-str vals)))

(defn var-val-map [bnd]
  (let [p2 (partition 2 bnd)
      vars (map first p2)
      nams (map name vars)]
  (zipmap nams vars)))

(defn to-bnd [vvm]
  (vec (reduce-kv #(concat %1[(symbol %2) %3]) [] vvm)))

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
  (vec (reduce #(concat %1 
	 [(symbol (sv %2 "variable")) 
	  (if-let [vals (seq (svs %2 "values"))]
	    (trans-vals vals)
	    (trans-obs (svs %2 "objects")))])
                         bnd 
                         (svs inp "input-table")))))

(defn do-code [pord bnd]
  (let [code (sv pord "code")
       bnd2 (read-string (str "[" (uncomment  code) "]"))
       bnd3 (vec (concat bnd bnd2))
       vvm1 (var-val-map bnd3)
       expr `(let ~bnd3 ~vvm1)
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

(defn do-next [inst bnd]
  (if (some? inst)
  (condp = (typ inst)
    "Process" (do-process inst bnd)
    "Decision" (do-decision inst bnd)
    "Input" (do-input inst bnd)
    (println (str "Unknown type: " typ)))))

