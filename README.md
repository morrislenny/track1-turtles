# welcometoclojurebridge

This repository has two apps.

1. A sample app to test your install for ClojureBridge
2. A template to try simple functions to walk a turtle

## Usage 1 - test your installation

LightTable - open `core.clj` and press
<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>Enter</kbd> or
<kbd>Cmd</kbd> + <kbd>Shift</kbd> + <kbd>Enter</kbd> to evaluate the file.

Emacs - run cider, open `core.clj` and press `C-c C-k` to evaluate the file.

REPL - run `(require 'welcometoclojurebridge.core)`.


## Usage 2 - try simple functions to walk a turtle

LightTable - open `walk.clj` and press
<kbd>Ctrl</kbd> + <kbd>Shift</kbd> + <kbd>Enter</kbd> or
<kbd>Cmd</kbd> + <kbd>Shift</kbd> + <kbd>Enter</kbd> to evaluate the file.

Emacs - run cider, open `walk.clj` and press `C-c C-k` to evaluate the
file. On repl, type `(ns clojurebridge-turtle.walk)`.

REPL - run `(require 'clojurebridge-turtle.walk)`, `(ns clojurebridge-turtle.walk)`.

#### How to move turtles

A turtle can move forward/backward and tilt its head right/left.
Combinations of these will make interesting lines.

See [outline/TURTLE.md](outline/TURTLE.md) for more details.


Original Works
--------------

"Welcome to ClojureBridge" app is originally written by @orb for
Austin, TX, USA workshop.
The original repository is <https://github.com/orb/welcometoclojurebridge>.


"Turtle" app is originally written by @echeran as Clojure port of Logo
programming language.
The original repository is <https://github.com/google/clojure-turtle>.


Many thanks to original authors!


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
