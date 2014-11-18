(ns kleffs.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [kleffs.world :as world]
            [kleffs.music :as music]
            [kleffs.artifact :as artifact]
            [kleffs.character :as character])
  (:use kleffs.utils)
  ;; (:use overtone.core)
  (:use overtone.live)
  (:gen-class))

;;(defn sequential-random-ints [min-step max-step]
;;  (iterate + ((rand-int max-step))))

(defn move-to-screen [screen-pos state]
  {:pre (vector? screen-pos)}
  ;(println screen-pos)
  (assoc
    state
    :current-screen-coords screen-pos
    :current-screen-data (get (:overworld state) screen-pos)
    ))

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
      :kleff (character/generate-kleff)})))

(defn on-key-pressed [{[screen-x screen-y]        :current-screen-coords
                       [world-width world-height] :world-size
                       :as state}
                      {raw-key :raw-key
                       key :key
                       :as event}]
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

   :else state))

(defn update [state]
  (music/update-music state))

(defn draw [{{[bg-h bg-s bg-v] :bg-color
              [fg-h fg-s fg-v] :fg-color
              rects :rects
              artifact :artifact}  :current-screen-data
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

  ; Draw artifact
  (if (not (nil? artifact))
    (artifact/draw-artifact artifact))

  ; Draw a metronome graphic
  (music/draw-metronome))

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
