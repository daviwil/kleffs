(ns kleffs.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [kleffs.world :as world]
            [kleffs.character :as character])
  (:use kleffs.utils)
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

;;(defn sequential-random-ints [min-step max-step]
;;  (iterate + ((rand-int max-step))))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)

  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)

  ; setup function returns initial state. It contains
  ; an initial set of rectangles
  (let [world-size [5 5]
        overworld (apply world/generate-overworld world-size)]
    (move-to-screen
     [0 0]
     {:overworld overworld
      :world-size world-size
      :current-screen-coords [0 0]
      :current-screen-data (get overworld [0 0])
      :kleff (character/generate-kleff) })))

(defn move-to-screen [screen-pos state]
  {:pre (vector? screen-pos)}
  (println screen-pos)
  (assoc
    state
    :current-screen-coords screen-pos
    :current-screen-data (get (:overworld state) screen-pos)
    ))

(defn on-key-pressed [state event]
  (let [{[screen-x screen-y]        :current-screen-coords
         [world-width world-height] :world-size} state]
    (cond
     (= (:raw-key event) \r) (assoc state :rects (world/generate-room-random))

     (= (:key event) :left)
       (move-to-screen [(dec-min screen-x 0) screen-y] state)

     (= (:key event) :right)
       (move-to-screen [(inc-max screen-x (dec world-width)) screen-y] state)

     (= (:key event) :up)
       (move-to-screen [screen-x (dec-min screen-y 0)] state)

     (= (:key event) :down)
       (move-to-screen [screen-x (inc-max screen-y (dec world-height))] state)

     (= (:raw-key event) \k) (assoc state :kleff (character/generate-kleff))

     :else state)))

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

(defn draw [{{[bg-h bg-s bg-v] :bg-color
              [fg-h fg-s fg-v] :fg-color
              rects :rects }  :current-screen-data
             screen-pos      :current-screen-coords
             kleff :kleff}]

  (q/background bg-h bg-s bg-v)

  ; Draw the rects list
  (q/fill fg-h fg-s fg-v)
  (q/no-stroke)
  (doseq [[x y width height] rects]
    (q/rect x y width height))

  ; Debug info
  (q/fill 0 0 255 200)  ; white with alpha
  (q/text-size 20)
  (q/text (format "Screen pos: %s" screen-pos) 10 490)
  (q/text "Arrow keys to change screen" 500 490)

  ; Draw kleff
  (character/draw-kleff kleff)

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

  :key-pressed on-key-pressed
  :middleware [m/fun-mode])

(defn -main
  [& args]
  (println "Make me set up the main sketch at some point later."))
