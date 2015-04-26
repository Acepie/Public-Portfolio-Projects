#lang racket

(require "Matrices.rkt" 2htdp/universe 2htdp/image lang/posn)
(define theta (/ pi 64))
(define background (bitmap/file "gradientbackground.jpg"))
(define show false)
(define click-limit 5)
(require images/compile-time)
(define instructions (bitmap/file "instructions.png"))
(define size (image-width background))
(define width size)
(define height size)

;; A world is a struct consisting of:
; - A Row Vector representing the players position
; - A Row Vector representing the current center
; - A Row Vector representing the balls current position
; - A number representing score
; - A number representing the number of clicks so far
; - A number representing the current combo level
; - A string of either:
; - running
; - paused
; - ended
(define-struct world (playerpos center ballpos score clicks combo state))

; is the player colliding with a ball
(define (colliding player ball)
  (> 20 (distance-matrix player ball)))

; create a new random ball
(define (new-ball)
  (list (list (+ 51 (random (- size 100))) (+ 51 (random (- size 100))))))

; calculate the next world on tick
(define (tick world)
  (cond [(not (equal? "running" (world-state world))) world]
        [(end world) (make-world (world-playerpos world)
                                 (world-center world)
                                 (world-ballpos world)
                                 (world-score world)
                                 (world-clicks world)
                                 (world-combo world)
                                 "ended")]
        [(colliding (world-playerpos world) (world-ballpos world))
         (make-world (rotate-point (world-playerpos world) 
                                   (world-center world) 
                                   (min (/ pi 8)
                                        (* (+ 1 (round (/ (world-score world) 32))) theta)))
                     (world-center world)
                     (new-ball)
                     (if (>= 1 (world-clicks world))
                         (max (round (* 1.4 (world-score world))) 
                              (add1 (world-score world)))
                         (min (round (* 1.4 (world-score world))) 
                              (add1 (world-score world))))
                     0
                     (if (>= 1 (world-clicks world))
                         (add1 (world-combo world))
                         1)
                     (world-state world))]
        [else
         (make-world (rotate-point (world-playerpos world) 
                                   (world-center world) 
                                   (max (min (/ pi 12)
                                             (* (+ 1 (round (/ (world-score world) 32))) theta))
                                        (/ pi -12)))
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world)
                     (world-state world))]))

; convert a row into a posn
(define (row->posn row)
  (make-posn (first (first row)) (second (first row))))

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
         (make-world (world-playerpos world)
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world)
                     "running")]
        [(mouse=? me "button-down")
         (let ((ball (circle (distance-matrix (world-playerpos world) 
                                              (list (list x y))) 
                             "outline" 
                             "black")))
           (if show
               (set! background (place-image ball x y background))
               (set! background background))
           (make-world (world-playerpos world)
                       (list (list x y))
                       (world-ballpos world)
                       (world-score world)
                       (add1 (world-clicks world))
                       (world-combo world)
                       (world-state world)))]
        [else world]))

; move the ball across the center on hitting space
(define (onkey world ke)
  (cond [(equal? (world-state world) "start")
         (make-world (world-playerpos world)
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world)
                     "running")]
        [(key=? ke "s")
         (set! show (not show))
         world]
        [(key=? ke "r")
         (set! background (bitmap/file "gradientbackground.jpg"))
         (make-world '((300 300)) '((300 300)) (new-ball) 0 0 1 "start")]
        [(key=? ke " ") 
         (set! theta (* -1 theta))
         world]
        [(and (key=? ke "p") 
              (equal? (world-state world) "running"))
         (make-world (world-playerpos world)
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world)
                     "paused")]
        [(and (key=? ke "p") 
              (equal? (world-state world) "paused"))
         (make-world (world-playerpos world)
                     (world-center world)
                     (world-ballpos world)
                     (world-score world)
                     (world-clicks world)
                     (world-combo world)
                     "running")]
        [else world]))

; is the game over
(define (end world)
  (or (equal? (world-clicks world) click-limit)
      (> (first (first (world-playerpos world))) size)
      (< (first (first (world-playerpos world))) 0)
      (> (second (first (world-playerpos world))) size)
      (< (second (first (world-playerpos world))) 0)))

(big-bang (make-world '((300 300)) '((300 300)) (new-ball) 0 0 1 "start")
          (on-tick tick)
          (to-draw render)
          (on-mouse change-center)
          (on-key onkey))