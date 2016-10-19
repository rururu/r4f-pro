(defproject r4f-pro "0.1.0-SNAPSHOT"
  :description "Clojure RETE implementation for frames"
  :url "https://github.com/rururu/r4f-pro"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [protege "3.5.0"]
                 [protege/standard-extensions "3.5.0"]
                 [protege/looks "3.5.0"]
                 [protege/unicode_panel "3.5.0"]
                 [protege/JGo "3.5.0"]
                 [protege/JGoLayout "3.5.0"]
                 [protege/ClojureTab "1.5.0"]
                 [rete "5.3.0-SNAPSHOT"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :repositories {"local" ~(str (.toURI (java.io.File. "repo")))}
  :aot [r4f-pro.core]
  :main r4f-pro.core)

