(ns world.basic
(:import 
  (java.awt Color Dimension)
  (javax.swing JPanel JFrame)
  (java.awt.event KeyListener KeyEvent)))

(def FIGURES (volatile! {}))
(defn draw-figure [[id func] g]
  (func g))

(defn draw-world [g]
  (doseq [fig (seq @FIGURES)]
  (draw-figure fig g)))

(defn quit [controls]
  (.dispose (:frame @controls)))

(defn slow-down [controls]
  (vswap! controls #(update-in % [:delay] inc)))

(defn speed-up [controls]
  (letfn [(dec-delay [delay]
	(if (> delay 0)
	  (dec delay)
	  delay))]
  (vswap! controls #(update-in % [:delay] dec-delay))))

(defn handle-key [c controls]
  (condp = c
  \q (quit controls)
  \s (slow-down controls)
  \f (speed-up controls)
  nil))

(defn world-panel [width height controls-atom]
  (proxy [JPanel KeyListener] []
  (paintComponent [g]
    (proxy-super paintComponent g)
    (.clearRect g 0 0 width height)
    (draw-world g))
  (keyPressed [e]
    (handle-key (.getKeyChar e) controls-atom)
    (.repaint this))
  (getPreferredSize []
    (Dimension. width height))
  (keyReleased [e])
  (keyTyped [e])))

(defn world-frame [width height]
  (let [controls-atom (volatile! {
	:frame (JFrame. "World Basic")
	:delay 1000
	:tick 0})
       panel (world-panel width height controls-atom)]
  (doto panel
    (.setFocusable true)
    (.addKeyListener panel))
  (doto (:frame @ controls-atom)
    (.add panel)
    (.pack)
    (.setVisible true)
    (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE))
  (future
    (while true
      (let [start-time (System/currentTimeMillis)]
        (println (:tick @controls-atom))
        (Thread/sleep (* 2 (:delay @controls-atom)))
        (vswap! controls-atom assoc :tick (inc (:tick @controls-atom)))
        (.repaint panel))))))

