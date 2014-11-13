(ns kleffs.character
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:use kleffs.utils)
  (:gen-class))

;; Kleffs Generator
(defn generate-circle []
  {:shape-type "circle"
   :color [(q/random 360) 100 100]
   :diameter (q/random 7 14)})

(defn generate-shape []
  (generate-circle))

(defn generate-kleff []
  (merge {:position [(int (/ (q/width) 2)) (int (/ (q/height) 2))]}
         (generate-shape)))

;; Only call this method from within quil-draw
(defn draw-kleff [kleff]
  (let [{shape-type :shape-type [hue saturation brightness] :color [x y] :position} kleff]
    (q/fill hue saturation brightness)
    (cond
     (= shape-type (str "circle")) (q/ellipse x y (:diameter kleff) (:diameter kleff)))))
