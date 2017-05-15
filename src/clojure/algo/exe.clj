(ns algo.exe
(:use protege.core)
(:require
  [rete.core :as rete]))

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

(defn trans-values [vals]
  (trans-obs (map val-from-str vals)))

(defn do-process [proc bnd]
)

