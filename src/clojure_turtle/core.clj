;; Copyright 2014 Google Inc. All Rights Reserved.

;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at

;;     http://www.apache.org/licenses/LICENSE-2.0

;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns clojure-turtle.core
  (:refer-clojure :exclude [repeat])
  (:require [quil.core :as q])
  (:use clojure.pprint))

;; turtles map
;; {:name {:x x :y y :angle a :pen truthy}
;; at the beginning, only :trinity is there
(def turtles (atom {:trinity {:x 0
                              :y 0
                              :angle 90
                              :pen true}}))

;; lines map
;; {:name [[[xs0 ys0] [xe0 ye0]] [[xs1 ys1] [xe1 ye1]]]}
;; at the beginning, only :trinity is there
(def lines (atom {:trinity []}))

(def turtle :trinity)

;; counter is used to name a new turtle
(def counter (agent 0))

(defn add-turtle
  "creates a new turtle with a name and adds to turtls map.
   if the name is not given, it will be :neo1, neo2, etc."
  ([]
     (send counter inc)
     (new-turtle (str "neo" @counter)))
  ([name]
     (let [n (keywrd name)]
       (swap! turtles assoc n {:x 0
                               :y 0
                               :angle 90
                               :pen true})
       (swap! lines assoc n []))))

(defn- update-thing
  [thing n f]
  (swap! thing update-in [n] f))

(defn- update-turtle
  "updates internal state of one of turtles
   n - name in key, :trinity, :neo1, :neo2, etc
   f - function which is applied to map"
  [n f]
  (update-thing turtles n f))

(defn- update-line
  "updates internal state of one of lines
   n - name in key, :trinity, :neo1, :neo2, etc
   f - function which is applied to vector"
  [n f]
  (update-thing lines n f))

(defn right
  "turns the specified turtle's head by given degrees in clockwise.
   if no name is given, only :trinity's head will be changed"
  ([a]
     (right turtle a))
  ([n a]
     (letfn [(add-angle
               [{:keys [angle] :as t}]
               (merge t {:angle (-> angle (- a) (mod 360))}))]
       (update-turtle n add-angle))))

(defn left
  "turns the specified turtle's head by given degrees in counterclockwise.
   if no name is given, only :trinity's head will be changed"
  ([a]
     (right (* -1 a)))
  ([n a]
     (right n (* -1 a))))

(def deg->radians q/radians)

(def radians->deg q/degrees)

(def atan q/atan)

(defn forward
  "moves the specified turtle forward by a given length.
   if no name is given, :trinity will go forward."
  ([len]
     (forward turtle len))
  ([n len]
     (let [rads      (fn [{:keys [angle]}]
                       (deg->radians angle))
           diffs     (fn [m]
                       (let [r (rads m)]
                         [(* len (Math/cos r)) (* len (Math/sin r))]))
           translate (fn [m]
                       (let [[dx dy] (diffs m)]
                         (if (or (not= 0 dx) (not= 0 dy))
                           (let [{:keys [x y]} m
                                 line          [[x y] [(+ x dx) (+ y dy)]]]
                             (update-line n (fn [v] (conj v line)))
                             (-> m (update-in [:x] + dx) (update-in [:y] + dy))))))]
       (update-turtle n translate))))

(defn backward
  "moves the specified turtle backward by a given length.
   if no name is given, :trinity will go backward."
  ([len]
     (forward (* -1 len)))
  ([n len]
     (forward n (* -1 len))))

(defn penup
  "changes the specified turtle's pen state to false.
   while the pen stays false, the turtle doesn't draw lines.
   if no name is given, :trinity's state will be changed."
  ([]
     (penup turtle))
  ([n]
     (update-turtle n (fn [m] (assoc m :pen false)))))

(defn pendown
  "changes the specified turtle's pen state to true.
   while the pen stays true, the turtle draws lines.
   if no name is given, :trinity's state will be changed."
  ([]
     (pendown turtle))
  ([n]
     (update-turtle n (fn [m] (assoc m :pen true)))))

(defn clean
  "cleans up all lines of the specified turtle.
   if no name is given, :trinity's lines will be cleaned up."
  ([]
     (clean turtle))
  ([n]
     (update-line n (constantly []))))

(defn clean-all
  "cleans up all lines of all turtles."
  ([]
     (swap! lines (fn [lm] (reduce-kv (fn [m k v] (assoc m k [])) {} lm)))))

(defn home
  "moves the specified turtle back to the home position.
   if no name is given, :trinity will be back home."
  ([]
     (home turtle))
  ([n]
     (update-turtle n (constantly {:x 0 :y 0 :angle 90 :pen true}))))

(defn home-all
  "moves all turtles back to the home position."
  ([]
     (swap! turtles (fn [tm]
                      (reduce-kv
                       (fn [m k v] (assoc m k {:x 0 :y 0 :angle 90 :pen true})) {} tm)))))

#_(defn- draw-turtle
  ([]
     (draw-turtle turtle))
  ([turt]
     (let [short-leg 5
           long-leg 12
           hypoteneuse (Math/sqrt (+ (* short-leg short-leg)
                                     (* long-leg long-leg)))
           large-angle  (-> (/ long-leg short-leg)
                            atan
                            radians->deg)
           small-angle (- 90 large-angle)
           pen-down? (get-in @turt [:pen])
           turt-copy (atom (assoc @turt :pen false))
           turt-copy-points (atom [])]
       (letfn [(record-turt-point
                 [t]
                 (let [new-x (get @t :x)
                       new-y (get @t :y)
                       new-point [new-x new-y]]
                   (swap! turt-copy-points conj new-point))
                 t)]
         (do
           (-> turt-copy
               record-turt-point
               (right 90)
               (forward short-leg)
               record-turt-point
               (left (- 180 large-angle))
               (forward hypoteneuse)
               record-turt-point
               (left (- 180 (* 2 small-angle)))
               (forward hypoteneuse)
               record-turt-point
               (left (- 180 large-angle))
               (forward short-leg)
               record-turt-point
               (left 90)))
         (let [lines (partition 2 1 @turt-copy-points)]
           (dorun
            (map (fn [line] (apply q/line (flatten line))) lines)))))))

(defmacro all
  [& body]
  `(fn []
     (do
       ~@ body)))

(defmacro repeat
  [n & body]
  `(let [states# (repeatedly ~n ~@body)]
     (dorun
      states#)
     (last states#)))

(defn reset-rendering
  []
  (.clear (q/current-graphics))
  (q/background 240)                 ;; Set the background colour to
                                     ;; a nice shade of grey.
  (q/stroke-weight 1)
  (q/stroke 222 79 79))

(defn setup []
  (q/smooth)                          ;; Turn on anti-aliasing
  ;; (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (reset-rendering))

(defn cursor-color [x y]
  "Calculate a color based on x and y values"
  (let [x-col (* 255 (/ (+ x (/ (q/width) 2)) (q/width)))
        y-col (* 255 (/ (+ y (/ (q/height) 2)) (q/height)))
        z-col (mod (+ x y) 255)]
    [x-col y-col z-col]))

(defn draw []
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (reset-rendering)

    (q/push-matrix)
    (q/apply-matrix 1  0 0
                    0 -1 0)
    (doseq [l @lines]
      (let [[[x1 y1] [x2 y2]] l]
        (apply q/stroke (cursor-color x2 y2))
        (q/line x1 y1 x2 y2)))
    (draw-turtle)
    (q/pop-matrix)))

(q/defsketch example                  ;; Define a new sketch named example
  :title "Watch the turtle go!"       ;; Set the title of the sketch
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  :features [:keep-on-top]            ;; Keep the window on top
  :size [485 300])                    ;; You struggle to beat the golden ratio



(comment
(def turtles (atom {:trinity {:x 0
                              :y 0
                              :angle 90
                              :pen true}}))
(new-turtle)  

  )
