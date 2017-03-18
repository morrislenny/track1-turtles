;; Needed to include the turtle graphics
(ns clojurebridge-turtle.walk
  (:use clojure.repl)
  (:use clojurebridge-turtle.core))

(defn draw-square [name length]
  (forward name length)
  (right name 90)
  (forward name length)
  (right name 90)
  (forward name length)
  (right name 90)
  (forward name length)
  (right name 90))

(defn point-up
  [name]
  (when (> (:angle (state name)) 180) (right name 180())))

(defn compute-distance
  [name]
  (Math/sqrt (+ (Math/pow (:x (state name)) 2) 
                (Math/pow (:y (state name)) 2))))

(defn no-turtle-left-behind
  [name]
  (when (> (compute-distance name) 150) (right name 180)))

(defn draw-and-turn
  [name length]
  (forward name length)
  (right name 90))

(defn box-spiral
  [name length]
  (when (> length 5)
    (draw-and-turn name length)
    (draw-and-turn name length)
    (box-spiral name (- length 5))))

(defn triangle 
  "makes a triangle of length length"
  [name length]
  (right name 60)
  (forward name length)
  (right name (- 180 60))
  (forward name length)
  (right name (- 180 60))
  (forward name length))

(defn pyramid
  [name length]
  (when (> length 5)
    (right name 90)
    (triangle name length)
    (right name -30)
    (pyramid name (- length 5))))

(defn triangle-height
  [length]
  (Math/sqrt (- (Math/pow length 2)
                (Math/pow (/ length 2) 2))))

(defn half-triangle
  [name length]
  (right name -90)
  (forward name (/ length 2))
  (right name 120)
  (forward name length)
  (right name -30))

(defn draw-tree
  [name length]
  (when (> length 5)
    (half-triangle name length)
    (right name 90)
    (triangle name length)
    (right name -30)
    (draw-tree name (- length 5))))

(defn move-to
  [name x y length]
  (forward name x)
  (right name 90)
  (forward name y)
  (right name -90)
  (clean name)
  (draw-tree name length))

(defn populate-turtles []
  (map #(add-turtle %) [:a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :trinity]))

(defn random-coordinate []
  (- (rand-int 150) (/ 150 2)))

(defn make-forest [length]
  (map #(move-to % (random-coordinate) (random-coordinate) length) (populate-turtles)))



;; You might want to start with (init) to make sure
;; that the canvas are in the initial state: 
;; only :trinity is on the canvas, at the center


