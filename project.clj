(defproject kleffs "0.1.0-SNAPSHOT"
  :description "A roguelike atmospheric platformer featuring procedural world, character, and music generation."
  :url "http://github.com/daviwil/kleffs"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [quil "2.2.2"]
                 [overtone "0.9.1"]]
  :main ^:skip-aot kleffs.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
