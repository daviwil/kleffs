(ns kleffs.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  ;(:use overtone.core)
  (:gen-class))

;; --- Overtone methods, don't call these just yet ---

;; Boot the Supercollider server
;(boot-external-server)

;; (definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
;;   (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
;;      (saw freq)
;;      vol))

;; (defn saw2 [music-note]
;;     (saw-wave (midi->hz (note music-note))))

;; (defn play-chord [a-chord]
;;   (doseq [note a-chord] (saw2 note)))

;; (defn chord-progression-time []
;;   (let [time (now)]
;;     (at time (play-chord (chord :C4 :major)))
;;     (at (+ 1000 time) (play-chord (chord :G3 :major)))
;;     (at (+ 2000 time) (play-chord (chord :F3 :sus4)))
;;     (at (+ 2700 time) (play-chord (chord :F3 :major)))
;;     (at (+ 3200 time) (play-chord (chord :G3 :major)))))

;; (defonce metro (metronome 120))
;; (comment
;;   (metro-bpm metro 120))

;; (def chord-progression
;;   [(chord :C4 :major)
;;    (chord :G3 :major)
;;    (chord :F3 :sus4)
;;    (chord :F3 :major)])

;; (defn chord-progression-beat [m beat-num]
;;   (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
;;   (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
;;   (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
;;   (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major)))
;; )

;; --- Quil functions ---

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)

  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)

  ; setup function returns initial state. It contains
  ; an initial set of rectangles
  {:rects
     (seq
      [[0 0 20 20]
       [30 30 60 360]
       [300 300 325 125]])
   }
  )


;;(defn sequential-random-ints [min-step max-step]
;;  (iterate + ((rand-int max-step))))

(defn calculate_width [x]
  (q/random x
            ;; the least of x + 1/5-screen-width and screen-width
            (min (+ x (/ (q/width) 5))
                 (q/width))))

(defn calculate_height [y height-change]
  (let [min-height (* (q/height) 0.7)]
    (q/random
     ;; the height of the previous rect +- between 100% and 50% of height-change
     (max (min (+ y (* (* height-change
                     (q/random 1.0 0.5))
                  (q/random -1 1)))
               min-height)
          (* (q/height) 0.5))
     min-height
     )))

;; Generates the rects for a room
(defn generate-rects [starting-x
                      starting-height-floor
                      height-change
                      ;; starting-height-ceiling,
                      ;; corridor-height
                      ]
  (loop [x starting-x
         y starting-height-floor
         rectangles ()]
    (if (>= x (q/width))
      rectangles
      (let [
            width (calculate_width x)
            height (calculate_height y height-change)
            ]
        (recur (+ x width) height (conj rectangles [x, height, width, (q/height)])))))

  ;; Generate blocks from left to right
  ;; some possibility of gaps between
  ;; Use up available screen space

  ;; Generation links
  ;; http://fbksoft.com/procedural-level-generation-for-a-2d-platformer/

  ;; *** The following section is some scratch code that doesn't work right yet ***

  ;; (take 5 (sequential-random-ints 1 5))

  ;; (reduce generate-rect
  ;;         { :min-x 0 :min-y 0
  ;;           :max-x (q/width) :max-y (q/height) }


  ;; Is there an appropriate higher order function to use here?
  ;; (loop [min-x 0 min-y 0
  ;;        max-x (q/width) max-y (q/height)
  ;;        rects []]
  ;;   (if (= min-x max-x)
  ;;     rects
  ;;     (recur

  ;;      (conj rects (generate-rect min-x min-y max-x max-y))

  ;; (let
  ;;     [fold-state {:x 0 :blocks []}
  ;;      block-height (q/random 50 200)]
  ;;   [[(:x fold-state) (- (q/height) block-height) (q/random 50 500) block-height]]
  ;; )
)

(defn on-key-typed [state event]
  (cond
   (= (:raw-key event) \r)
   (assoc state :rects (generate-rects 0 (* (q/random 0.8 0.5) (q/height)) 200))
   :else state))

(defn update [state]

  ;; Just return the state without touching it for now
  state

  ;; --- These will be added back when Overtone is turned back on

  ;; (let
  ;;     [beat (mod (metro-beat metro) (metro-bpb metro))]

  ;;   ;; (if (not= (:beat state) beat)
  ;;   ;;   (at (now) (play-chord (chord-progression beat))))

  ;;   (assoc
  ;;     state
  ;;     :beat beat))
)

(defn draw [state]
  ; Clear the sketch by filling it with a light blue color
  (q/background 150 125 255)

  ; Draw the rects list
  (q/fill 30 255 150)
  (q/no-stroke)
  (doseq [[x y width height] (:rects state)]
    (q/rect x y width height))

  ;; -- This draws a metronome graphic when it is on.  Uncomment
  ;; -- this once we start using Overtone again.
  ;; (let
  ;;     [beat (mod (metro-beat metro) (metro-bpb metro))
  ;;      offset (+ 30 (* beat 30))]
  ;;  (q/fill offset 255 255)
  ;;  (q/ellipse offset 30 30 30))
)

(q/defsketch kleffs
  :title "Kleffs"
  :size [800 500]

  ; setup function called only once, during sketch initialization.
  :setup setup

  ; update is called on each iteration before draw is called.
  ; It updates sketch state.
  :update update
  :draw draw

  :key-typed on-key-typed
  :middleware [m/fun-mode])

(defn -main
  [& args]
  (println "Make me set up the main sketch at some point later."))
