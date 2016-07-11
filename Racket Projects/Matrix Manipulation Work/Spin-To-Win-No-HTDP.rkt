#lang racket

(require "Matrices-2.rkt" racket/gui/base math)
(define theta (/ pi 64))
(define background (read-bitmap "gradientbackground.jpg"))
(define click-limit 5)
(define instructions (read-bitmap "instructions.png"))
(define size (send background get-width))
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
(struct world (playerpos center ballpos score clicks combo state) #:mutable)

; create a new random ball
(define (new-ball)
  (row-matrix [(+ 51 (random (- size 100))) (+ 51 (random (- size 100)))]))

(define base-world (world (row-matrix [(/ size 2) (/ size 2)])
                          (row-matrix [(/ size 2) (/ size 2)])
                          (new-ball) 0 0 1 "start"))

(define current-world (struct-copy world base-world))

; is the player colliding with a ball
(define (colliding player ball)
  (> 20 (distance-matrix player ball)))

; calculate the next world on tick
(define (tick)
  (cond [(not (equal? "running" (world-state current-world)))
         current-world]
        [(end current-world)
         (set-world-state! current-world "ended")
         current-world]
        [(colliding (world-playerpos current-world) (world-ballpos current-world))
         (set-world-ballpos! current-world (new-ball))
         (set-world-score! current-world
                           (if (>= 1 (world-clicks current-world))
                               (max (round (* 1.4 (world-score current-world))) 
                                    (add1 (world-score current-world)))
                               (min (round (* 1.4 (world-score current-world))) 
                                    (add1 (world-score current-world)))))
         (set-world-clicks! current-world 0)
         (set-world-combo! current-world
                           (if (>= 1 (world-clicks current-world))
                               (add1 (world-combo current-world))
                               1))
         current-world]
        [else
         (set-world-playerpos! current-world
                               (rotate-player current-world))
         current-world])
  (send canvas refresh))

; rotate player around center
(define (rotate-player world)
  (rotate-point (world-playerpos world) 
                (world-center world) 
                (max (min (/ pi 12)
                          (* (+ 1 (round (/ (world-score world) 32)))
                             theta))
                     (/ pi -12))))

; get the X coord of a row matrix
(define (row->x row)
  (matrix-ref row 0 0))

; get the Y coord of a row matrix
(define (row->y row)
  (matrix-ref row 0 1))

; draw world
(define (render canvas dc)
  (send dc clear)
  (cond [(equal? (world-state current-world) "start")
         (send dc draw-bitmap instructions
               (- (/ size 2) (/ (send instructions get-width) 2))
               (- (/ size 2) (/ (send instructions get-height) 2)))]
        [(equal? (world-state current-world) "paused")
         (render-gui dc)
         (send dc set-pen "black" 20 'solid)
         (let-values ([(w h d s) (send dc get-text-extent "paused")])
           (send dc draw-text "paused"
                 (- (/ size 2) (/ w 2))
                 (- (/ size 2) (/ h 2)))
           )]
        [(equal? (world-state current-world) "ended")
         (render-gui dc)
         (send dc set-pen "black" 20 'solid)
         (let-values ([(w h d s) (send dc get-text-extent (string-append "Game Over! You scored: "
                                                                         (~a (world-score current-world))))])
           (send dc draw-text (string-append "Game Over! You scored: "
                                             (~a (world-score current-world)))
                 (- (/ size 2) (/ w 2))
                 (- (/ size 2) (/ h 2))))]
        [else (render-gui dc)]))

; draw basic world information onto drawing context
(define (render-gui dc)
  (send dc draw-bitmap background 0 0)
  (send dc set-pen "black" 20 'solid)
  (let-values ([(w h d s) (send dc get-text-extent
                                (string-append "combo: " (~a (sub1 (world-combo current-world)))))])
    (send dc draw-text (string-append "combo: " (~a (sub1 (world-combo current-world))))
          (- width 100)
          (- height 75 h)))
  (let-values ([(w h d s) (send dc get-text-extent
                                (string-append "score: " (~a (world-score current-world))))])
    (send dc draw-text (string-append "score: " (~a (world-score current-world)))
          (- width 100)
          (- height 100 h)))
  (send dc set-pen "blue" 20 'solid)
  (send dc draw-point
        (row->x (world-ballpos current-world)) (row->y (world-ballpos current-world)))
  (send dc set-pen "black" 5 'solid)
  (send dc draw-point
        (row->x (world-center current-world)) (row->y (world-center current-world)))
  (send dc set-pen "red" 40 'solid)
  (send dc draw-point
        (row->x (world-playerpos current-world)) (row->y (world-playerpos current-world)))
  (send dc set-pen "black" 15 'solid)
  (let-values ([(w h d s) (send dc get-text-extent (~a (- click-limit (world-clicks current-world))))])
    (send dc draw-text (~a (- click-limit (world-clicks current-world)))
          (- (row->x (world-playerpos current-world)) (/ w 2))
          (- (row->y (world-playerpos current-world)) (/ h 2)))))

; move the center to the posnition of the mouse click
(define (change-center canvas me)
  (cond [(and (equal? (world-state current-world) "start")
              (send me button-down? 'left))
         (set-world-state! current-world "running")]
        [(send me button-down? 'left)
         (set-world-center! current-world (row-matrix [(send me get-x) (send me get-y)]))
         (set-world-clicks! current-world (add1 (world-clicks current-world)))]
        [else 0]))

; move the ball across the center on hitting space
(define (onkey canvas ke)
  (cond [(equal? (world-state current-world) "start")
         (set-world-state! current-world "running")]
        [(eq? (send ke get-key-code) #\r)
         (set! background (read-bitmap "gradientbackground.jpg"))
         (set! current-world (struct-copy world base-world))
         (send canvas refresh)]
        [(eq? (send ke get-key-code) #\space) 
         (set! theta (* -1 theta))
         (send canvas refresh)]
        [(and (eq? (send ke get-key-code) #\p) 
              (equal? (world-state current-world) "running"))
         (set-world-state! current-world "paused") (send canvas refresh)]
        [(and (eq? (send ke get-key-code) #\p) 
              (equal? (world-state current-world) "paused"))
         (set-world-state! current-world "running") (send canvas refresh)]
        [else (send canvas refresh)]))

; is the game over
(define (end world)
  (or (equal? (world-clicks world) click-limit)
      (> (matrix-ref (world-playerpos world) 0 0) size)
      (< (matrix-ref (world-playerpos world) 0 0) 0)
      (> (matrix-ref (world-playerpos world) 0 1) size)
      (< (matrix-ref (world-playerpos world) 0 1) 0)))

(define toplevel (new frame% [label "Spin To Win"] [width width] [height height]))

(define canvas (new
                (class canvas% (init) (super-new)
                  (define/override (on-char event)
                    (onkey this event))
                  (define/override (on-event event)
                    (change-center this event)))
                [parent toplevel]
                [stretchable-width width]
                [stretchable-height height]
                [paint-callback render]))

(send toplevel show #t)
(send canvas focus)

(define timer (new timer%
                   [notify-callback tick]
                   [interval 10]))