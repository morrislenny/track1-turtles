(ns welcometoclojurebridge.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))



(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  (q/text-font (q/create-font "Helvetica" 32))
  #_(q/load-image "logo.png")

  {:color 0
   :angle 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

(defn draw-state [state]
  (q/background 250)

  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    (q/fill 180 255 255)
    (q/text "Welcome to"     140 (min 180 (+ 190 y)))
    (q/text "ClojureBridge!" 130 (max 330 (+ 330 y)))

    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      (q/fill (:color state) 255 255)

      (q/ellipse x y 100 100))))

(q/defsketch welcometoclojurebridge
  :title "Welcome To ClojureBridge!"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])

