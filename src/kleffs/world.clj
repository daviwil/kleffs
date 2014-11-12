(ns kleffs.world
  (:require [quil.core :as q]
            [quil.middleware :as m])
  ;(:use overtone.core)
  (:gen-class))

(defn generate-ground [min-x min-y]
  [min-x
   min-y
   (min (+ min-x
           (int (q/random (/ (q/width) 100) (/ (q/width) 50))))
        (q/width))
   (q/height)])

(defn generate-ceiling [min-x width max-y]
  ;;  (generate-rect min-x 0 width (q/width) max-y )
  [min-x 0 (+ min-x width) max-y])

(defn generate-room [starting-x
                     starting-height-floor
                     height-change
                     corridor-height
                     underground]
  (loop [min-x starting-x
         prev-y starting-height-floor
         rectangles ()]
    (if (>= min-x (q/width))
      rectangles
      (let [min-y (max (min (+ prev-y (* (* height-change
                                            (q/random 1.5 0.5))
                                         (q/random -1 1)))
                            (* (q/height) 0.95))
                       (* (q/height) 0.6))
            ground (generate-ground min-x min-y)
            width (first (rest (rest ground)))
            ceiling (if (true? underground)
                      ;; Most of the ceiling block dim is based on the floor below it (To avoid bad collisions)
                      (generate-ceiling (first ground) width (- min-y corridor-height))
                      [])
            ]
        (recur (+ min-x width) min-y (conj rectangles (conj ground ceiling)))))))

(defn generate-room-random []
  (generate-room 0 (* (q/random 0.8 0.5) (q/height)) 200 30 true))

