# Turtles App API (Application Programming Interface)

## Basics

- At the beginning, only one turtle whose name is `:trinity` (don't omit `:`
(colon), it is a Clojure keyword) will show up at the home position (center).
This turtle, `:trinity`, can move forward/backward,
change head angle clockwise/counterclockwise.

![initial state](img/initial-state.png)


- More turtles can be added, each with its own name.

- Each turtle can move independently giving a command with its name.
If only `:trinity` is there, you don't need to give the name to commands.


## Movement

A turtle can move in response to commands. When a turtle moves, it leaves a trail. You can move turtles, change their colors, and get neat drawings this way. 

All parameters to `forward`, `backward`, `right`, and `left` commands
are relative to the current position or angle. The command may be given the name of the turtle. Without the name they would refer to the only turtle on the canvas.


| command | description |           |
| ------- | ----------- | ----------|
|`(forward length)` <br /> `(forward name length)`| moves the turtle forward by length.| ![go forward](img/go-forward.png) |
|`(backward length)` <br /> `(backward name length)`| moves the turtle backward by length.| ![go backward](img/go-backward.png) |
|`(right angle)` <br /> `(right name angle)`| changes the turtle head by degrees clockwise.|![tilt right](img/right.png) |
|`(left angle)` <br /> `(left name angle)`| changes the turtle head by degrees counterclockwise.|![tilt left](img/left.png) |
|`(set-color color)` <br /> `(set-color name color)`| changes the color of the turtle to a color,  <br /> such as `:red` or `:orange`. <br />This only works for colors that are already defined,  <br /> see the list below||
|`(set-color r g b)` <br /> `(set-color name r g b)`| changes the color of the turtle to any combination of <br /> red, green, blue color components.||
|`(undo)` <br /> `(undo name)`| undoes the last line and back the turtle.||
|`(home)` <br /> `(home name)`| moves the turtle back to the home position.||
|`(home-all)`| moves all turtles back to the home position.||



### usage examples

```clojure
(forward 30)         ;; :trinity moves forward by 30 pixels when only :trinity is there
(forward :neo 40)    ;; :neo moves

(backward 35)        ;; :trinity moves backward by 35 pixels when only  :trinity is there
(backward :neo 100)  ;; :neo moves

(right 45)           ;; :trinity turns 45 degrees clockwise when only :trinity is there
(right :neo 90)      ;; :neo turns 90 degrees clockwise

(left 30)            ;; :trinity turns 30 degrees counterclockwise when only  :trinity is there
(left :neo 135)      ;; :neo turns 135 degrees counterclockwise

(set-color :red)  ;; :trinity is now red when only :trinity is there
(set-color :neo :green) ;; :neo is now green

(set-color 255 0 0)  ;; :trinity is now red when only :trinity is there
(set-color :neo 0 255 0) ;; :neo is now green

(undo)               ;; :trinity's last line will be removed when only :trinity is there
(undo :neo)          ;; :neo's last line will be removed

(home)               ;; moves :trinity back to the home position, center  when only :trinity is there
(home :neo)          ;; moves :neo back to the home position, center

(home-all)           ;; moves all turtles back to the home position
```
## Colors

You can use the following color names:
``
:lime :orange :white :yellow :navy :green :cyan :gold :red :blue :maroon :pink :teal :magenta :purple :silver :grey :brown :black
``
You can also specify your own colors by providing their RGB (Red, Green, Blue) encoding. For instance, `0	245	255` is a turquoise color. We discuss colors more in the turtles lesson. 

## Turtle

| command | description |
| ------- | ----------- |
|`(add-turtle name)`| adds a turtle with its name.|
|`(turtle-names)`| returns all turtle names.|
|`(state) (state name)`| returns a current state of the turtle.|
|`(state-all)`| returns current states of all turtles.|

Turtle's state includes its current coordinates (x,y), the angle it's pointing at, its color, and its name: 
`{:x 0, :y 0, :angle 135, :color :purple, :name :trinity}`



### usage examples

```clojure
(add-turtle :neo)    ;; adds a turtle whose name is :neo

(turtle-names)       ;; returns all turtle names

(state)              ;; returns :trinity's current state when only :trinity is there.

(state :neo)         ;; returns :neo's current state

(state-all)          ;; returns all turtles' current states
```

The `state` functions show the *absolute* coordinates on the canvas. 
<!--
not coordinates relative to the starting point that are
used in `forward`, `backward`, `right` and `left`.
-->
For example, the starting state of :trinity is `{:x 0, :y 0, :angle 90, :color :purple, :name :trinity}`.

Absolute dimensions and angles are set up as shown below:


##### x and y in absolute values

| position | x and y values |
| -------- | ---------------------------------------- |
|home      | both x and y are zero. `(x, y) = (0, 0)` |
|rightmost | x is 240, y is any. `(x, y) = (240, y)` |
|leftmost  | x is -240, y is any. `(x, y) = (-240, y)` |
|top       | x is any, y is 150. `(x, y) = (x, 150)` |
|bottom    | x is any, y is -150. `(x, y) = (x, -150)` |

![axises](img/axes.png)


##### angle in absolute degrees

| head     | angle |
| -------- | ----- |
| right    | 0 |
| up       | 90 |
| left     | 180 |
| down     | 270 |

![angles](img/angles.png)


## Clean up

| command | description |
| ------- | ----------- |
|`(clean) (clean name)`| cleans all lines belong to the turtle. |
|`(clean-all)`| cleans all lines of all turtles. |

### usage examples

```clojure
(clean)              ;; cleans all of :trinity's lines only when :trinity is there
(clean :neo)         ;; cleans all of :neo's lines

(clean-all)          ;; cleans all turtles all lines
```

## Initialize

| command | description |
| ------- | ----------- |
|`(init)`| sets the canvas back to the staring state. |

### usage examples

```clojure
(init)               ;; sets the canvas back to the starting state, only :trinity is in home position
```

## Samples

[How to Walk Turtles](TURTLE-SAMPLES.md) has more examples as well as
function study materials.

License
-------
<a rel="license"
href="http://creativecommons.org/licenses/by/4.0/deed.en_US"><img
alt="Creative Commons License" style="border-width:0"
src="http://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br
/><span xmlns:dct="http://purl.org/dc/terms/"
href="http://purl.org/dc/dcmitype/Text" property="dct:title"
rel="dct:type">ClojureBridge Curriculum</span> by <span
xmlns:cc="http://creativecommons.org/ns#"
property="cc:attributionName">ClojureBridge</span> is licensed under a
<a rel="license"
href="http://creativecommons.org/licenses/by/4.0/deed.en_US">Creative
Commons Attribution 4.0 International License</a>.
