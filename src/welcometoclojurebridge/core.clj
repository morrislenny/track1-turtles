(ns welcometoclojurebridge.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def angle-speed 0.05)
(def color-speed 0.07)

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  (q/text-font (q/create-font "Helvetica" 32))

  {:logo (q/load-image "logo.png")
   :color 0
   :angle 0})

(defn update-state [state]
  (-> state
      (update-in [:color] #(mod (+ % color-speed) 255))
      (update-in [:angle] + angle-speed)))

(defn draw-state [state]
  (q/background 255)

  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    (q/fill (:color state) 255 255)
    (q/text "Welcome to"     140 (min 180 (+ 190 y)))
    (q/text "You are ready to code!" 80 (max 330 (+ 330 y)))

    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      (q/image (:logo state) (- x 50) (- y 50) 100 100))))

(q/defsketch welcometoclojurebridge
  :title "Welcome To ClojureBridge!"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])


