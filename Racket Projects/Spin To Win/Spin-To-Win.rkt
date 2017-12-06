#lang racket

(require math
         lux
         lux/chaos/gui
         lux/chaos/gui/key
         lux/chaos/gui/mouse
         pict
         "Matrices.rkt")

(define theta (/ pi 64))
(define background (bitmap "gradientbackground.jpg"))
(define click-limit 5)
(define instructions (bitmap "instructions.png"))
(define width (pict-width background))
(define height (pict-height background))

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
(struct world
  (playerpos center ballpos score clicks combo state)
  #:mutable
  #:methods gen:word
  [(define (word-fps w)
     60.0)
   (define (word-label s ft)
     (lux-standard-label "Spin To Win" ft))
   (define (word-output w)
     (render w))
   (define (word-event w e)
     (cond
       [(key-event? e) (onkey w e)]
       [(mouse-event? e) (onmouse w e)]
       [else w]))
   (define (word-tick w)
     (tick w))])

; create a new random ball
(define (new-ball w h)
  (row-matrix [(+ 51 (random (- w 100))) (+ 51 (random (- h 100)))]))

(define (base-world w h)
  (world (row-matrix [(/ w 2) (/ h 2)])
         (row-matrix [(/ w 2) (/ h 2)])
         (new-ball w h) 0 0 1 "start"))

; is the player colliding with a ball
(define (colliding player ball)
  (> 20 (distance-matrix player ball)))

; calculate the next world on tick
(define (tick current-world)
  (cond [(not (equal? "running" (world-state current-world)))
         current-world]
        [(end current-world width height)
         (set-world-state! current-world "ended")
         current-world]
        [(colliding (world-playerpos current-world) (world-ballpos current-world))
         (set-world-ballpos! current-world (new-ball width height))
         (set-world-score! current-world
                           (update-score current-world))
         (set-world-combo! current-world
                           (if (< 1 (world-clicks current-world))
                               1
                               (add1 (world-combo current-world))))
         (set-world-clicks! current-world 0)
         current-world]
        [else
         (set-world-playerpos! current-world
                               (rotate-player current-world))
         current-world]))

(define (update-score current-world)
  (if (>= 1 (world-clicks current-world))
      (max (round (* 1.4 (world-score current-world))) 
           (add1 (world-score current-world)))
      (min (round (* 1.4 (world-score current-world))) 
           (add1 (world-score current-world)))))

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
(define (render current-world)
  (λ (w h dc)
    (send dc clear)
    (cond [(equal? (world-state current-world) "start")
           (draw-pict instructions dc
                      (- (/ w 2) (/ (pict-width instructions) 2))
                      (- (/ h 2) (/ (pict-height instructions) 2)))]
          [(equal? (world-state current-world) "paused")
           (render-gui current-world dc w h)
           (render-world current-world dc)
           (let-values ([(tw th d s) (send dc get-text-extent "paused")])
             (draw-pict (text "paused" null 20) dc
                        (- (/ w 2) (/ tw 2))
                        (- (/ h 2) (/ th 2))))]
          [(equal? (world-state current-world) "ended")
           (render-gui current-world dc w h)
           (render-world current-world dc)
           (let*-values ([(str) (string-append "Game Over! You scored: " (~a (world-score current-world)))]
                         [(tw th d s) (send dc get-text-extent str)])
             (draw-pict (text str null 20) dc
                        (- (/ w 2) (/ tw 2))
                        (- (/ h 2) (/ th 2))))]
          [else 
           (render-gui current-world dc w h)
           (render-world current-world dc)])))

; draw basic world information onto drawing context
(define (render-gui current-world dc width height)
  (draw-pict background dc 0 0)
  (let*-values ([(str) (string-append "combo: " (~a (sub1 (world-combo current-world))))]
                [(w h d s) (send dc get-text-extent str)])
    (draw-pict (text str null 20) dc
               (- width 100 (/ w 2))
               (- height 75 h)))
  (let*-values ([(str) (string-append "score: " (~a (world-score current-world)))]
                [(w h d s) (send dc get-text-extent str)])
    (draw-pict (text str null 20) dc
               (- width 100 (/ w 2))
               (- height 100 h))))

; draw world objects such as player, ball, and center
(define (render-world current-world dc)
  (define GOAL_SIZE 20)
  (define CENTER_SIZE 5)
  (define PLAYER_SIZE 40)
  (draw-pict (disk GOAL_SIZE #:color "blue") dc
             (- (row->x (world-ballpos current-world)) (/ GOAL_SIZE 2))
             (- (row->y (world-ballpos current-world)) (/ GOAL_SIZE 2)))
  (draw-pict (disk CENTER_SIZE #:color "black") dc
             (- (row->x (world-center current-world)) (/ CENTER_SIZE 2))
             (- (row->y (world-center current-world)) (/ CENTER_SIZE 2)))
  (draw-pict (disk PLAYER_SIZE #:color "red") dc
             (- (row->x (world-playerpos current-world)) (/ PLAYER_SIZE 2))
             (- (row->y (world-playerpos current-world)) (/ PLAYER_SIZE 2)))
  (let-values ([(w h d s) (send dc get-text-extent (~a (- click-limit (world-clicks current-world))))])
    (draw-pict (text (~a (- click-limit (world-clicks current-world))) null 15) dc
               (- (row->x (world-playerpos current-world)) (/ w 2))
               (- (row->y (world-playerpos current-world)) (/ h 2)))))

; move the center to the position of the mouse click
(define (onmouse current-world me)
  (cond [(and (equal? (world-state current-world) "start")
              (send me button-down? 'left))
         (set-world-state! current-world "running")
         current-world]
        [(send me button-down? 'left)
         (set-world-center! current-world (row-matrix [(send me get-x) (send me get-y)]))
         (set-world-clicks! current-world (add1 (world-clicks current-world)))
         current-world]
        [else current-world]))

; move the ball across the center on hitting space
(define (onkey current-world ke)
  (cond [(equal? (world-state current-world) "start")
         (set-world-state! current-world "running")
         current-world]
        [(eq? (send ke get-key-code) #\r)
         (set! background (bitmap "gradientbackground.jpg"))
         (set! current-world (base-world width height))
         current-world]
        [(eq? (send ke get-key-code) #\space) 
         (set! theta (* -1 theta))
         current-world]
        [(and (eq? (send ke get-key-code) #\p) 
              (equal? (world-state current-world) "running"))
         (set-world-state! current-world "paused")
         current-world]
        [(and (eq? (send ke get-key-code) #\p) 
              (equal? (world-state current-world) "paused"))
         (set-world-state! current-world "running")
         current-world]
        [else current-world]))

; is the game over
(define (end world w h)
  (or (equal? (world-clicks world) click-limit)
      (> (matrix-ref (world-playerpos world) 0 0) w)
      (< (matrix-ref (world-playerpos world) 0 0) 0)
      (> (matrix-ref (world-playerpos world) 0 1) h)
      (< (matrix-ref (world-playerpos world) 0 1) 0)))

(define (start w)
  (fiat-lux w))

(module+ main
  (call-with-chaos
   (make-gui #:width width #:height height)
   (λ ()
     (start (base-world width height)))))