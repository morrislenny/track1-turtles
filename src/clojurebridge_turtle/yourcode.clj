;; Needed to include the turtle graphics
(ns clojurebridge-turtle.walk
  (:use clojure.repl)
  (:use clojurebridge-turtle.core))

;; You might want to start with (init) to make sure
;; that the canvas are in the initial state: 
;; only :trinity is on the canvas, at the center
;(init)

(defn draw-square [name]
  (forward name 100)
  (right name 90)
  (forward name 100)
  (right name 90)
  (forward name 100)
  (right name 90)
  (forward name 100)
  (right name 90))

  
  

