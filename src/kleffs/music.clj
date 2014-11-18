(ns kleffs.music
  (:require [quil.core :as q])
  ;; (:use overtone.core)
  (:use overtone.live))

;; Boot the Supercollider server -- only needed if using overtone.core
;;(boot-external-server)

(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (env-lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(defn saw2 [music-note]
    (saw-wave (midi->hz (note music-note))))

(defn play-chord [a-chord]
  (doseq [note a-chord] (saw2 note)))

(defn chord-progression-time []
  (let [time (now)]
    (at time (play-chord (chord :C4 :major)))
    (at (+ 1000 time) (play-chord (chord :G3 :major)))
    (at (+ 2000 time) (play-chord (chord :F3 :sus4)))
    (at (+ 2700 time) (play-chord (chord :F3 :major)))
    (at (+ 3200 time) (play-chord (chord :G3 :major)))))

(defonce metro (metronome 120))
(comment
  (metro-bpm metro 120))

(def chord-progression
  [(chord :C4 :major)
   (chord :G3 :major)
   (chord :F3 :sus4)
   (chord :F3 :major)])

(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major)))
)

(defn update-music [state]
  state)

(defn draw-metronome []
  (let
      [beat (mod (metro-beat metro) (metro-bpb metro))
       offset (+ 30 (* beat 30))]
   (q/fill offset 255 255)
   (q/ellipse offset 30 30 30)))
