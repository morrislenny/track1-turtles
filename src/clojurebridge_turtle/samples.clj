(ns clojure-turtle.samples
  (:use clojure-turtle.core))
(clean-all)
(home-all)
(add-turtle)
(add-turtle)
(add-turtle :neo)

(defn four-turtles
  []
  (clean-all)
  (home-all)
  (left :smith0 45)
  (right :smith1 45)
  (right :neo 90)
  (doseq [n (turtle-names)]
    (forward n 50)
    (forward n 50)
    (right n 90)
    (forward n 50)))
