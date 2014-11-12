(ns kleffs.utils)

(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))

(defn inc-max [value max-value]
  (min (inc value) max-value))

(defn dec-min [value min-value]
  (max (dec value) min-value))

(defn rand-between [min-value max-value]
  (+ (rand (- max-value min-value)) min-value))

(defn rand-color
  [[min-hue max-hue] [min-sat max-sat] [min-bright max-bright]]
  [(rand-between min-hue max-hue)
   (rand-between min-sat max-sat)
   (rand-between min-bright max-bright)])
