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
   if the name is not given, it will be :smith0, smith1, etc."
  ([]
     (add-turtle (str "smith" @counter))
     (send counter inc))
  ([name]
     (let [n (keyword name)]
       (swap! lines assoc n [])
       (swap! turtles assoc n {:x 0
                               :y 0
                               :angle 90
                               :pen true}))))

(defn turtle-names
  "returns turtle names"
  []
  (keys @turtles))

(defn- update-thing
  [thing n f]
  (swap! thing update-in [n] f))

(defn- update-turtle
  "updates internal state of one of turtles
   n - name in key, :trinity, :smith1, smith2, etc.
   f - function which is applied to map"
  [n f]
  (update-thing turtles n f))

(defn- update-line
  "updates internal state of one of lines
   n - name in key, :trinity, :smith1, smith2, etc.
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
                           (let [{:keys [x y pen]} m
                                 line              [[x y] [(+ x dx) (+ y dy)]]]
                             (if pen (update-line n (fn [v] (conj v line))))
                             (-> m (update-in [:x] + dx) (update-in [:y] + dy))))))]
       (update-turtle n translate))))

(defn backward
  "moves the specified turtle backward by a given length.
   if no name is given, :trinity will go backward."
  ([len]
     (forward (* -1 len)))
  ([n len]
     (forward n (* -1 len))))

(defn undo
  "undos the specified turtle's last move.
   if no name is given, :trinity's move will be undoed."
  ([]
     (undo turtle))
  ([n]
     (update-line n (fn [v] (-> v butlast vec)))
     (let [[x y] (-> (n @lines) last last)]
       (update-turtle n (fn [m] (merge m {:x x :y y}))))))

(defn penup
  "changes the specified turtle's pen state to false.
   while the pen stays false, the turtle doesn't draw lines.
   if no name is given, :trinity's state will be changed."
  ([]
     (penup turtle))
  ([n]
     (update-turtle n (fn [m] (merge m {:pen false})))))

(defn pendown
  "changes the specified turtle's pen state to true.
   while the pen stays true, the turtle draws lines.
   if no name is given, :trinity's state will be changed."
  ([]
     (pendown turtle))
  ([n]
     (update-turtle n (fn [m] (merge m {:pen true})))))

(defn pendown?
  "returns true or false whether the specified turtle's pen is down or not.
   if not name is given, :trinity's state will be returned."
  ([]
     (pendown? turtle))
  ([n]
     (-> @turtles n :pen)))

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

;;
;; head
;; x = r1 * cos(theta) + x0
;; y = r1 * sin(theta) + y0
;; where (x0, y0): turtle's :x, :y
;;       theta: turtle's :angle
;;       r1: lr (long radius)
;;
;; bottom left
;; x = r2 * cos(theta + 90) + x0
;; y = r2 * sin(theta + 90) + y0
;; where (x0, y0): turtle's :x, :y
;;       theta: turtle's :angle
;;       r2: sr (short radius)
;;
;; bottom right
;; x = r2 * cos(theta - 90) + x0
;; y = r2 * sin(theta - 90) + y0
;; where (x0, y0): turtle's :x, :y
;;       theta: turtle's :angle
;;       r2: sr (short radius)
;;

(def lr 12)
(def sr 5)

(defn- three-coords
  "returns a vector of three coordinates which form a turtle,
   [head bottom-left bottom-right]"
  [{:keys [x y angle]}]
  (let [ah  (deg->radians angle)
        abl (deg->radians (mod (+ angle 90) 360))
        abr (deg->radians (mod (- angle 90) 360))]
    [[(+ x (* lr (q/cos ah)))  (+ y (* lr (q/sin ah)))]
     [(+ x (* sr (q/cos abl))) (+ y (* sr (q/sin abl)))]
     [(+ x (* sr (q/cos abr))) (+ y (* sr (q/sin abr)))]]))

(defn- draw-turtle
  "draws a single turtle"
  [m]
  (let [[h bl br] (three-coords m)
        es     [[h bl] [bl br] [br h]]
        three   (map flatten es)]
    (q/stroke 50 50 50)
    (doseq [line three] (apply q/line line))))

(defn- draw-all-turtles
  "draws all turtles"
  []
  (let [ms (vals @turtles)]
    (doseq [m ms] (draw-turtle m))))

(defn- cursor-color [x y]
  "calculates a color based on x and y values"
  (let [x-col (* 255 (/ (+ x (/ (q/width) 2)) (q/width)))
        y-col (* 255 (/ (+ y (/ (q/height) 2)) (q/height)))
        z-col (mod (+ x y) 255)]
    [x-col y-col z-col]))

(defn- draw-lines
  "draws lines of a single turtle"
  [v]
  (doseq [l v]
    (let [[[x1 y1] [x2 y2]] l]
      (apply q/stroke (cursor-color x2 y2))
      (q/line x1 y1 x2 y2))))

(defn- draw-all-lines
  "draws all lines of all turtles"
  []
  (let [vs (vals @lines)]
    (doseq [v vs] (draw-lines v))))

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
  (q/stroke-weight 1))

(defn setup []
  (q/smooth)                          ;; Turn on anti-aliasing
  ;; (q/frame-rate 1)                    ;; Set framerate to 1 FPS
  (reset-rendering))

(defn draw []
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (reset-rendering)

    (q/push-matrix)
    (q/apply-matrix 1  0 0
                    0 -1 0)
    (draw-all-lines)
    (draw-all-turtles)
    (q/pop-matrix)))

(q/defsketch example                  ;; Define a new sketch named example
  :title "Watch the turtle go!"       ;; Set the title of the sketch
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  :features [:keep-on-top]            ;; Keep the window on top
  :size [485 300])                    ;; You struggle to beat the golden ratio

