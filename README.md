# r4f-pro

Integrated Development Environment for [rete4frames] (https://github.com/rururu/rete4frames) rule engine and expert system shell based on [Protege-3.5 ontology editor] (http://protege.stanford.edu):

![screenshot](screenshot.jpg)


## Usage

Simple start:
```clj
$ cd <..>/r4f-pro
$ lein run
```
Start IDE for developers
```clj
$ cd <..>/r4f-pro
$ lein repl
...
rete.protege=> (-main)
```

3 minute IDE [screencast](https://www.youtube.com/watch?v=RZKKq6Pym44&feature=youtu.be).

## Algorithms

In a project Algorithm.pprj collected functionality for visual creation of algorithms. Algorithms are created using drag-and-drop of standard blocks from a palette on a canvas of a flow-chart and following connection them by stream lines. After that the standard blocks are filled with clojure code as of "let" macro body.  

Important distinction of these algorithms from a common notion of algorithm in that they can represent parallel processes. For this purpose to standard blocks added two new ones: "Concurrent" and "Wait". Full list of blocks is following:
..* Input
..* Process
..* Decision
..* Predefined processes
..* Concurrent
..* Wait

If you want to add this functionality to your own project you need to include Algorithm.pprj project into it. This can be done by "Project -> Manage Included Projects.." menu item.

Algorithm examples in AlgorithmExamples.pprj project.

Creation of simplest algorithm [screencast](https://youtu.be/oRCMw_rnLvg)

## License

Copyright © 2016 Ruslan Sorokin.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
[License of Protege-3.5](https://github.com/rururu/r4f-pro/blob/master/LICENSE_PROTEGE)
