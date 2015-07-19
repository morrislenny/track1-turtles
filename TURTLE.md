# Turtles application commands

## Basics

- At the beginning, only one turtle whose name is :trinity (don't omit ':'
(colon), it is a Clojure keyword) will show up at the home position (center).
This turtle, :trinity, can move forward/backward,
change head angle clockwise/counterclockwise, or jump.

- Turtles can be added with/without a name.
If no name is given, a new turtle's name will be :smith0, :smith1,
etc.

- Each turtle can move independently giving a command with the name.
If no name is given, :trinity is the turtle to receive a command.


## Movement

| command | description |
| ------- | ----------- |
|`(forward len) (forward n len)`| moves the turtle forward by len.|
|`(backward len) (backward n len)`| moves the turtle backward by len.|
|`(right a) (right n a)`| changes the turtle head by a degrees clockwise.|
|`(left a) (left n a)`| changes the turtle head by a degrees counterclockwise.|
|`(undo) (undo n)`| undos the last line and back the turtle.|
|`(home) (home n)`| moves the turtle back to the home position.|
|`(home-all)`| moves all turtles back to the home position.|


## Pen

| command | description |
| ------- | ----------- |
|`(pen-up) (pen-up n)`| changes the pen state false. combination with
`forward` gives a jumping effect.|
|`(pen-down) (pen-down n)`| changes the pen state true and draws a
line afterwards.|


## Turtle

| command | description |
| ------- | ----------- |
|`(add-turtle) (add-turtle n)`| adds a turtle. if no name, the turtle
will be named, :smith0, :smith1, ...|
|`(turtle-names)`| returns all turtle names.|
|`(state?) (state? n)`| returns a current state of the turtle.|


## Clean up

| command | description |
| ------- | ----------- |
|`(clean) (clean n)`| cleans all lines belong to the turtle. |
|`(clean-all)`| cleans all lines of all turtles. |


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
