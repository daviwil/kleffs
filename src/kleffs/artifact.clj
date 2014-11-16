(ns kleffs.artifact
  (:require [quil.core :as q]
            [kleffs.shape :as shape])
  (:gen-class))

(defn generate-artifact []
  {:shape (shape/generate-shape "circle")
   :position [(int (/ (q/width) 2)) (int (/ (q/height) 3))]})

;; This method provides a random suggestion as to where to place artifacts
(defn generate-artifacts [[x y :as map-dimensions]
                          number-of-artifacts]
  (into {}
        (loop [artifacts []]
          (if (= number-of-artifacts (count artifacts))
            artifacts
            (recur (conj artifacts
                         [[(int (q/random x)) (int (q/random y))] (generate-artifact)]))
            ))))

(defn draw-artifact [{shape :shape
                      position :position}]
  (shape/draw-shape shape position))
