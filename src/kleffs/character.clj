(ns kleffs.character
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [kleffs.shape :as shape])
  (:gen-class))

;; Kleffs Generator
(defn generate-kleff []
  {:position [(int (/ (q/width) 2)) (int (/ (q/height) 2))]
   :shape (shape/generate-shape "circle")})

;; Only call this method from within quil-draw
(defn draw-kleff [{shape :shape
                   position :position}]
  (shape/draw-shape shape position))
