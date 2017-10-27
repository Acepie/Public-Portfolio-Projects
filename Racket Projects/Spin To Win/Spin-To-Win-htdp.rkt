#lang racket

(require "Matrices.rkt" 2htdp/universe 2htdp/image lang/posn math images/compile-time)
(define theta (/ pi 64))
(define background (bitmap/file "gradientbackground.jpg"))
(define show false)
(define click-limit 5)
(define instructions (bitmap/file "instructions.png"))
(define size (image-width background))
(define width size)
(define height size)

;; A world is a struct consisting of:
; - A Row Matrix representing the players position
; - A Row Matrix representing the current center
; - A Row Matrix representing the balls current position
; - A number representing score
; - A number representing the number of clicks so far
; - A number representing the current combo level
; - A string of either:
; - running
; - paused
; - ended
(define-struct world (playerpos center ballpos score clicks combo state) #:mutable)

; is the player colliding with a ball
(define (colliding player ball)
  (> 20 (distance-matrix player ball)))

; create a new random ball
(define (new-ball)
  (row-matrix [(+ 51 (random (- size 100))) (+ 51 (random (- size 100)))]))

; calculate the next world on tick
(define (tick world)
  (cond [(not (equal? "running" (world-state world)))
         world]
        [(end world)
         (set-world-state! world "ended")
         world]
        [(colliding (world-playerpos world) (world-ballpos world))
         (set-world-ballpos! world (new-ball))
         (set-world-score! world
                           (if (>= 1 (world-clicks world))
                               (max (round (* 1.4 (world-score world))) 
                                    (add1 (world-score world)))
                               (min (round (* 1.4 (world-score world))) 
                                    (add1 (world-score world)))))
         (set-world-clicks! world 0)
         (set-world-combo! world
                           (if (>= 1 (world-clicks world))
                               (add1 (world-combo world))
                               1))
         world]
        [else
         (set-world-playerpos! world
                               (rotate-point (world-playerpos world) 
                                             (world-center world) 
                                             (max (min (/ pi 12)
                                                       (* (+ 1 (round (/ (world-score world) 32))) theta))
                                                  (/ pi -12))))
         world]))

; convert a row into a posn
(define (row->posn row)
  (make-posn (matrix-ref row 0 0) (matrix-ref row 0 1)))

; draw the current world
(define (render world)
  (let ((base (place-images (list (overlay (text (~a (- click-limit (world-clicks world)))
                                                 15
                                                 "black")
                                           (circle 20 "solid" "red"))
                                  (circle 5 "outline" "black")
                                  (circle 10 "solid" "blue")
                                  (text (string-append "score: " (~a (world-score world)))
                                        20
                                        "black")
                                  (text (string-append "combo: " (~a (sub1 (world-combo world))))
                                        20
                                        "black"))
                            (list (row->posn (world-playerpos world))
                                  (row->posn (world-center world))
                                  (row->posn (world-ballpos world))
                                  (make-posn (- width 100) (- height 50))
                                  (make-posn (- width 100) (- height 25)))
                            background)))
    (cond [(equal? (world-state world) "start") (overlay instructions
                                                         background)]
          [(equal? (world-state world) "paused") (overlay (text "paused"
                                                                20
                                                                "black")
                                                          base)]
          [(equal? (world-state world) "ended") 
           (overlay (text (string-append "Game Over! You scored: "
                                         (~a (world-score world)))
                          20
                          "black")
                    base)]
          [else base])))

; move the center to the posnition of the mouse click
(define (change-center world x y me)
  (cond [(and (equal? (world-state world) "start")
              (mouse=? me "button-down"))
         (set-world-state! world "running") world]
        [(mouse=? me "button-down")
         (let ((orbit-path (circle (distance-matrix (world-playerpos world) 
                                                    (row-matrix [x y])) 
                                   "outline" 
                                   "black")))
           (if show
               (set! background (place-image orbit-path x y background))
               (set! background background))
           (set-world-center! world (row-matrix [x y]))
           (set-world-clicks! world (add1 (world-clicks world))))
         world]
        [else world]))

; move the ball across the center on hitting space
(define (onkey world ke)
  (cond [(equal? (world-state world) "start")
         (set-world-state! world "running") world]
        [(key=? ke "s")
         (set! show (not show))
         world]
        [(key=? ke "r")
         (set! background (bitmap/file "gradientbackground.jpg"))
         (make-world (row-matrix [300 300]) (row-matrix [300 300]) (new-ball) 0 0 1 "start")]
        [(key=? ke " ") 
         (set! theta (* -1 theta))
         world]
        [(and (key=? ke "p") 
              (equal? (world-state world) "running"))
         (set-world-state! world "paused") world]
        [(and (key=? ke "p") 
              (equal? (world-state world) "paused"))
         (set-world-state! world "running") world]
        [else world]))

; is the game over
(define (end world)
  (or (equal? (world-clicks world) click-limit)
      (> (matrix-ref (world-playerpos world) 0 0) size)
      (< (matrix-ref (world-playerpos world) 0 0) 0)
      (> (matrix-ref (world-playerpos world) 0 1) size)
      (< (matrix-ref (world-playerpos world) 0 1) 0)))

(big-bang (make-world (row-matrix [300 300]) (row-matrix [300 300]) (new-ball) 0 0 1 "start")
          (on-tick tick)
          (to-draw render)
          (on-mouse change-center)
          (on-key onkey))
