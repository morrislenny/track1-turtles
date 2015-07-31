;; Creative Commons Attribution 4.0 International (CC BY 4.0)
;;
;; This turtle app is a ClojureBridge dialect of
;; clojure-turtle  https://github.com/google/clojure-turtle by @echeran
;; ClojureBridge version is focusing on more Clojure-ish style.
;;
(ns clojurebridge-turtle.core
  (:require [quil.core :as q]))

;; turtles map
;; {:name {:x x :y y :angle a :color [r g b]}
;; at the beginning, only :trinity is there
(def turtles (atom {:trinity {:x 0
                              :y 0
                              :angle 90
                              :color [30 30 30]}}))

;; lines map
;; {:name [[xs0 ys0 xe0 ye0] [xs1 ys1 xe1 ye1]]}
;; at the beginning, only :trinity is there
(def lines (atom {:trinity []}))

(def turtle :trinity)

;; counter is used to name a new turtle
(def counter (atom -1))

(defn add-turtle
  "creates a new turtle with a name and adds to turtls map.
   if the name is not given, it will be :smith0, smith1, etc.
   additionally, allows to choose color, which is a vector of [r g b]"
  ([]
     (swap! counter inc)
     (add-turtle (str "smith" @counter) [10 107 30]))
  ([name]
     (add-turtle (keyword name) [75 0 130]))
  ([name color]
     (let [n (keyword name)]
       (dosync
        (swap! lines assoc n [])
        (swap! turtles assoc n {:x 0
                                :y 0
                                :angle 90
                                :color color}))
       [n (n @turtles)])))

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
     (update-turtle n (fn [m] (update-in m [:angle] (comp #(mod % 360) #(- % a)))))
     [n a]))

(defn left
  "turns the specified turtle's head by given degrees in counterclockwise.
   if no name is given, only :trinity's head will be changed"
  ([a]
     (right (* -1 a)))
  ([n a]
     (right n (* -1 a))))

(defn forward
  "moves the specified turtle forward by a given length.
   if no name is given, :trinity will go forward."
  ([len]
     (forward turtle len))
  ([n len]
     (let [rads      (fn [{:keys [angle]}]
                       (q/radians angle))
           diffs     (fn [m]
                       (let [r (rads m)]
                         [(* len (Math/cos r)) (* len (Math/sin r))]))
           translate (fn [m]
                       (let [[dx dy] (diffs m)]
                         (if (or (not= 0 dx) (not= 0 dy))
                           (let [{:keys [x y]} m
                                 line          [x y (+ x dx) (+ y dy)]]
                             (update-line n (fn [v] (conj v line)))
                             (-> m (update-in [:x] + dx) (update-in [:y] + dy))))))]
       (update-turtle n translate)
       [n len])))

(defn backward
  "moves the specified turtle backward by a given length.
   if no name is given, :trinity will go backward."
  ([len]
     (forward (* -1 len)))
  ([n len]
     (forward n (* -1 len))))

(defn undo
  "undos the specified turtle's last line and moves the turtle back.
   if no name is given, :trinity's move will be undoed."
  ([]
     (undo turtle))
  ([n]
     (if (< 0 (-> @lines n count))
       (do
         (update-line n (fn [v] (-> v butlast vec)))
         (if-let [[_ _ x y] (-> @lines n last)]
           (update-turtle n (fn [m] (merge m {:x x :y y})))
           (update-turtle n (fn [m] (merge m {:x 0 :y 0}))))))
     n))

(defn state
  "returns a current state of the specified turtle.
   if no name is given, :trinity's state will be returned."
  ([]
     (state turtle))
  ([n]
     [n (n @turtles)]))

(defn clean
  "cleans up all lines of the specified turtle.
   if no name is given, :trinity's lines will be cleaned up."
  ([]
     (clean turtle))
  ([n]
     (update-line n (constantly []))
     n))

(defn clean-all
  "cleans up all lines of all turtles."
  ([]
     (swap! lines (fn [lm] (reduce-kv (fn [m k v] (assoc m k [])) {} lm)))
     (turtle-names)))

(defn home
  "moves the specified turtle back to the home position.
   if no name is given, :trinity will be back home."
  ([]
     (home turtle))
  ([n]
     (update-turtle n (fn [m] (merge m {:x 0 :y 0 :angle 90})))
     n))

(defn home-all
  "moves all turtles back to the home position."
  ([]
     (swap! turtles (fn [tm]
                      (reduce-kv
                       (fn [m k v] (assoc m k (merge v {:x 0 :y 0 :angle 90}))) {} tm)))
     (turtle-names)))

(defn init
  "makes back to the starting state.
   only :trinity is in the home position."
  []
  (swap! lines (constantly {turtle []}))
  (swap! turtles (constantly {turtle {:x 0
                                      :y 0
                                      :angle 90
                                      :color [30 30 30]}}))
  turtle)

;; triangle (by polar equations)
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
  (let [ah  (q/radians angle)
        abl (q/radians (mod (+ angle 90) 360))
        abr (q/radians (mod (- angle 90) 360))]
    [[(+ x (* lr (q/cos ah)))  (+ y (* lr (q/sin ah)))]
     [(+ x (* sr (q/cos abl))) (+ y (* sr (q/sin abl)))]
     [(+ x (* sr (q/cos abr))) (+ y (* sr (q/sin abr)))]]))

(defn- draw-turtle
  "draws a single turtle"
  [m]
  (let [[h bl br] (three-coords m)
        p2p       [[h bl] [bl br] [br h]]
        three     (map flatten p2p)]
    (apply q/stroke (:color m))
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
        z-col (mod (/ (+ x-col y-col) 2) 255)]
    [x-col y-col z-col]))

(defn- draw-lines
  "draws lines of a single turtle"
  [v]
  (doseq [l v]
    (let [[x1 y1 x2 y2] l]
      (apply q/stroke (cursor-color x2 y2))
      (q/line x1 y1 x2 y2))))

(defn- draw-all-lines
  "draws all lines of all turtles"
  []
  (let [vs (vals @lines)]
    (doseq [v vs] (draw-lines v))))

(defn reset-rendering
  []
  (.clear (q/current-graphics))
  (q/background 240)                 ;; Set the background colour to
                                     ;; a nice shade of light grey.
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

(q/defsketch clojurebridge-turtle
  :title "Walk your turtles!"         ;; Set the title of the sketch
  :setup setup                        ;; Specify the setup fn
  :draw draw                          ;; Specify the draw fn
  :features [:keep-on-top]            ;; Keep the window on top
  :size [485 300])                    ;; You struggle to beat the golden ratio

