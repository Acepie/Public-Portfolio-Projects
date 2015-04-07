#lang racket

(require "Matrices.rkt" 2htdp/universe 2htdp/image lang/posn)
(define theta (/ pi 36))
(define size 600)
(define width size)
(define height size)
(define background (empty-scene width height))

;; A world is a struct consisting of:
; - A Row Vector representing the players position
; - A Row Vector representing the current center
; - A Row Vector representing the balls current position
; - A number representing score
(define-struct world (playerpos center ballpos score clicks combo))

; is the player colliding with a ball
(define (colliding player ball)
  (> 20 (sqrt (+ (sqr (- (first (first player)) 
                         (first (first ball))))
                 (sqr (- (second (first player)) 
                         (second (first ball))))))))

; create a new random ball
(define (new-ball)
  (list (list (+ 26 (random (- size 50))) (+ 26 (random (- size 50))))))

; calculate the next world on tick
(define (tick world)
  (cond [(colliding (world-playerpos world) (world-ballpos world))
         (make-world (rotate-point (world-playerpos world) 
                                   (world-center world) 
                                   theta)
                     (world-center world)
                     (new-ball)
                     (+ (world-combo world) (world-score world))
                     0
                     (if (>= 1 (world-clicks world))
                         (add1 (world-combo world))
                         1))]
        [else
         (make-world (rotate-point (world-playerpos world) 
                                   (world-center world) 
                                   theta)
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world))]))

; convert a row into a posn
(define (row->posn row)
  (make-posn (first (first row)) (second (first row))))

; draw the current world
(define (render world)
  (place-images (list (circle 20 "solid" "red")
                      (circle 5 "outline" "black")
                      (circle 10 "solid" "blue")
                      (text (string-append "score: " (~a (world-score world)))
                            20
                            "black")
                      (text (string-append "combo: " (~a (sub1 (world-combo world))))
                            20
                            "black")
                      (text (string-append "clicks: " (~a (world-clicks world)))
                            20
                            "black"))
                (list (row->posn (world-playerpos world))
                      (row->posn (world-center world))
                      (row->posn (world-ballpos world))
                      (make-posn (- width 50) (- height 50))
                      (make-posn (- width 50) (- height 25))
                      (make-posn (- width 50) (- height 75)))
                background))

; move the center to the posnition of the mouse click
(define (change-center world x y me)
  (if (mouse=? me "button-down")
      (make-world (world-playerpos world)
                  (list (list x y))
                  (world-ballpos world)
                  (world-score world)
                  (add1 (world-clicks world))
                  (world-combo world))
      world))

; move the ball across the center on hitting space
(define (reflect-across world ke)
  (cond [(key=? ke " ") 
         (make-world (reflect-across-point (world-playerpos world)
                                           (world-center world))
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world))]
        [else world]))

; is the game over
(define (end world)
  (or (> (first (first (world-playerpos world))) size)
      (< (first (first (world-playerpos world))) 0)
      (> (second (first (world-playerpos world))) size)
      (< (second (first (world-playerpos world))) 0)))

; make game over screen
(define (endpic world)
  (overlay (text (string-append "Game Over! You scored: "
                                (~a (world-score world)))
                 20
                 "black")
           background))

(big-bang (make-world '((300 300)) '((300 300)) (new-ball) 0 0 1)
          (on-tick tick)
          (to-draw render)
          (on-mouse change-center)
          (on-key reflect-across)
          (stop-when end endpic))