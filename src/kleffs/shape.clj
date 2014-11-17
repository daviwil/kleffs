(ns kleffs.shape
  (:require [quil.core :as q])
  (:gen-class))

(defn generate-circle []
  {:shape-type "circle"
   ;; TODO: Color is too often brown.  might be better to give q/random (if possible) a list
   ;; of choices rather than the spectrum from which to choose
   :color [(int (q/random 360)) 100 100]
   :diameter (q/random 7 14)})

(defn generate-shape [shape-type]
  (cond
   (= 0 (compare "circle" shape-type)) (generate-circle)
   ))

;; Only call this method from within quil-draw
(defn draw-shape [{shape-type :shape-type
                   [hue saturation brightness] :color
                   :as shape} [x y]]
  (q/fill hue saturation brightness)
  (cond
   (= shape-type (str "circle")) (q/ellipse x y (:diameter shape) (:diameter shape))))
