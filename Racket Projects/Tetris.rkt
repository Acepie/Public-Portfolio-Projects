;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname Tetris) (read-case-sensitive #t) (teachpacks ((lib "universe.rkt" "teachpack" "2htdp") (lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "universe.rkt" "teachpack" "2htdp") (lib "image.rkt" "teachpack" "2htdp")))))
;; Problem 1: Tetris Revisited

;; DATA DEFINITIONS

;; A Block is a (make-block Number Number Color)
(define-struct block (x y color))

;; A Tetra is a (make-tetra Posn [ListOf Block])
;; The center point is the point around which the tetra rotates
;; when it spins.
(define-struct tetra (center blocks))

;; A World is a (make-world Tetra [ListOf Block] Number)
;; The [ListOf Block] (formerly BSet) represents the pile of blocks at the bottom of the screen.
;; The Tetra represents the current tetra controlled by the player
;; The Number represents the players total score throughout the game
(define-struct world (tetra pile score))

;; A Direction is one of:
;; - -1
;; - 1

;; CONSTANTS
(define GRID-SQ 20)
(define GRID-WIDTH 10)
(define GRID-HEIGHT 20)
(define SCENE (empty-scene (* GRID-SQ GRID-WIDTH) (* GRID-SQ GRID-HEIGHT)))

; Starting Blocks:

(define O-BLOCK (make-tetra (make-posn 5.5 -0.5)
                            (list (make-block 5 -1 "green")
                                  (make-block 6 -1 "green")
                                  (make-block 5 0 "green")
                                  (make-block 6 0 "green"))))
(define I-BLOCK (make-tetra (make-posn 5 0)
                            (list (make-block 4 0 "blue")
                                  (make-block 5 0 "blue")
                                  (make-block 6 0 "blue")
                                  (make-block 7 0 "blue"))))
(define L-BLOCK (make-tetra (make-posn 6 0)
                            (list (make-block 4 0 "purple")
                                  (make-block 5 0 "purple")
                                  (make-block 6 0 "purple")
                                  (make-block 6 -1 "purple"))))
(define J-BLOCK (make-tetra (make-posn 4 0)
                            (list (make-block 4 -1 "cyan")
                                  (make-block 4 0 "cyan")
                                  (make-block 5 0 "cyan")
                                  (make-block 6 0 "cyan"))))
(define T-BLOCK (make-tetra (make-posn 5 0)
                            (list (make-block 4 0 "orange")
                                  (make-block 5 0 "orange")
                                  (make-block 5 -1 "orange")
                                  (make-block 6 0 "orange"))))
(define Z-BLOCK (make-tetra (make-posn 5 -1)
                            (list (make-block 4 -1 "pink")
                                  (make-block 5 -1 "pink")
                                  (make-block 5 0 "pink")
                                  (make-block 6 0 "pink"))))
(define S-BLOCK (make-tetra (make-posn 5 -1)
                            (list (make-block 4 0 "red")
                                  (make-block 5 0 "red")
                                  (make-block 5 -1 "red")
                                  (make-block 6 -1 "red"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Tetris

; Rendering:

; place-image/grid : Image Number Number Image -> Image
; Place img1 on img2 with x & y converted from grid coordinates to pixels
(define (place-image/grid img1 x y img2)
  (place-image img1 (- (* x GRID-SQ) (quotient GRID-SQ 2))
               (- (* y GRID-SQ) (quotient GRID-SQ 2))
               img2))

; draw-block : Block -> Image
; Draw Block b
(define (draw-block b)
  (overlay (square GRID-SQ "outline" "black")
           (square GRID-SQ "solid" (block-color b))))

; block+scene : Block Image -> Image
; Draw Block b on scene
(define (block+scene b scene)
  (place-image/grid (draw-block b)
                    (block-x b) (block-y b)
                    scene))

; bset+scene : [ListOf Block] Image  -> Image
; Draw all Blocks in bs on scene
(define (bset+scene bs scene)
  (foldr block+scene scene bs))

; tetra+scene : Tetra Image -> Image
; Draw the Tetra t on scene
(define (tetra+scene t scene)
  (bset+scene (tetra-blocks t) scene))

; world->scene : World -> Image
; Draw the World w
(define (world->scene w)
  (tetra+scene (world-tetra w) (bset+scene (world-pile w) SCENE)))

; Key Handling:

; key-buffer : World KeyEvent -> World
; Confirms that ke performs a valid movement of the tetra before changing the World w
(define (key-buffer w ke)
  (local ((define changedWorld (key-handler w ke)))
    (cond [(valid-world? changedWorld) changedWorld]
          [else w])))

; key-handler : World KeyEvent -> World
; Rotate or shift the tetra in w based on ke
(define (key-handler w ke)
  (local ((define pile (world-pile w))
          (define score (world-score w))
          (define tetra (world-tetra w)))
    (cond [(string=? "left" ke) (make-world (shift-tetra tetra -1) pile score)]
          [(string=? "right" ke) (make-world (shift-tetra tetra 1) pile score)]
          [(string=? "a" ke) (make-world (rotate-ccw tetra) pile score)]
          [(string=? "s" ke) (make-world (rotate-cw tetra) pile score)]
          [else w])))

; valid-world? : World -> Boolean
; Is the tetra within the left and right borders and are there no collisions?
(define (valid-world? w)
  (local ((define blocks (tetra-blocks (world-tetra w))))
    (and (blocks-in-bounds? blocks)
         (not (collisions? blocks (world-pile w))))))

; blocks-in-bounds? : [ListOf Block] -> Boolean
; Are all of the blocks in bs in-bounds?
(define (blocks-in-bounds? bs)
  (andmap (λ (b) (and (<= (block-x b) 10)
                      (> (block-x b) 0)
                      (<= (block-y b) 20)
                      (> (block-y b) 0)))
          bs))

; collisions? : [ListOf Block] [ListOf Block] -> Boolean
; Are none of the blocks in bs1 in the same position as a block in bs2?
(define (collisions? bs1 bs2)
  (local (; Block Block -> Boolean
          ; Are b1 and b2 in the same position?
          (define (block-pos-same? b1 b2)
            (and (= (block-x b1) (block-x b2))
                 (= (block-y b1) (block-y b2)))))
    
    (ormap (λ (block1)
             (ormap (λ (block2)
                      (block-pos-same? block1 block2))
                    bs2))
           bs1)))

; LEFT AND RIGHT MOVEMENT:

; shift-tetra : Tetra Direction -> Tetra
; Shift t left or right according to dir
(define (shift-tetra t dir)
  (make-tetra (make-posn (+ (posn-x (tetra-center t)) dir)
                         (posn-y (tetra-center t)))
              (shift-blocks (tetra-blocks t) dir)))

; shift-blocks : [ListOf Block] Direction -> [ListOf Block]
; Shift blocks in bs left or right according to dir
(define (shift-blocks bs dir)
  (local (; Block -> Block
          ; Shift b left or right according to dir
          (define (shift-block b)
            (make-block (+ (block-x b) dir)
                        (block-y b)
                        (block-color b))))
    (map shift-block bs)))

; ROTATIONS

; rotate-ccw : Tetra -> Tetra
; Rotate t 90 degrees counterclockwise around its center
(define (rotate-ccw t)
  (rotate-cw (rotate-cw (rotate-cw t))))

; rotate-cw : Tetra -> Tetra
; Rotate t 90 degrees clockwise around its center
(define (rotate-cw t)
  (make-tetra (tetra-center t)
              (blocks-rotate-cw (tetra-center t) (tetra-blocks t))))

; blocks-rotate-cw : Posn [ListOf Block] -> [ListOf Block]
; Rotate all blocks in bs 90 degrees clockwise around c
(define (blocks-rotate-cw c bs)
  (local(; Posn Block -> Block
         ; Rotate b 90 degrees clockwise around c
         (define (block-rotate-cw b)
           (make-block (+ (posn-x c)
                          (- (posn-y c)
                             (block-y b)))
                       (+ (posn-y c)
                          (- (block-x b)
                             (posn-x c)))
                       (block-color b))))
    (map block-rotate-cw bs)))

; Every Tick:

; next-world : World -> World
; Create the subsequent world
(define (next-world w)
  (local ((define tetra (world-tetra w))
          (define score (world-score w))
          
          ; add-to-pile : World -> [ListOf Block]
          ; Append blocks in tetra to pile
          (define (add-to-pile w)
            (append (tetra-blocks tetra)
                    (world-pile w))))
    (cond [(landed? w) (make-world (new-tetra (random 7))
                                   (remove-full-rows (add-to-pile w))
                                   (+ 4 score))]
          [else (make-world (tetra-fall tetra)
                            (world-pile w)
                            score)])))

; new-tetra : Number -> Tetra
; Create a new tetra just off the top of the screen
; n must be in [0,6]
(define (new-tetra n)
  (cond [(= 0 n) O-BLOCK]
        [(= 1 n) I-BLOCK]
        [(= 2 n) L-BLOCK]
        [(= 3 n) J-BLOCK]
        [(= 4 n) T-BLOCK]
        [(= 5 n) S-BLOCK]
        [(= 6 n) Z-BLOCK]))

; TETRA FALLING

; tetra-fall : Tetra -> Tetra
; Lower t by one square
(define (tetra-fall t)
  (local ((define tc (tetra-center t))
          ; [ListOf Block] -> [ListOf Block]
          ; Lower blocks in bs by one square
          (define (lower-blocks bs)
            (map (λ (b) (make-block (block-x b) (add1 (block-y b)) (block-color b))) bs)))
    
    (make-tetra (make-posn (posn-x tc)
                           (add1 (posn-y tc)))
                (lower-blocks (tetra-blocks t)))))

; CHECK IF BLOCK HAS LANDED

; landed? : World -> Boolean
; Is w in a landed scenario?
(define (landed? w)
  (local ((define t (world-tetra w))
          ;[ListOf Block] -> Boolean
          ;determine if blocks are on floor
          (define (floor? bs)
            (ormap (λ (block) (= (block-y block) 20)) bs)))
    
    (or (collisions? (tetra-blocks (tetra-fall t)) (world-pile w))
        (floor? (tetra-blocks t)))))

; Row Removal

; remove-full-rows: [ListOf Block] -> [ListOf Block]
; remove full rows from bs
(define (remove-full-rows bs)
  (local (; row-full?: Number [ListOf Block] -> Boolean
          ; Is the row at y value n full?
          (define (row-full? n bs)
            (= 10 (length (filter (λ (block) (= n (block-y block))) bs))))
          ;Number -> [ListOf Block]
          ;remove blocks with a y of n
          (define (remove-row n)
            (filter (λ (block) (not (= n (block-y block)))) bs))
          ;Block -> Block
          ;Move unlanded block down
          (define (land-block b)
            (make-block (block-x b)
                        (add1 (block-y b))
                        (block-color b)))
          ;[ListOf Block] Number -> [ListOf Block] 
          ;shift all blocks above removed row down 1 square
          (define (shift-above bset n)
            (map (λ (block) (if (< (block-y block) n)
                                (land-block block)
                                block))
                 bset))
          (define rows-to-remove
            (filter (λ (n) (row-full? n bs)) (build-list 20 add1))))
    (cond [(empty? rows-to-remove) bs]
          [else (remove-full-rows (shift-above (remove-row (first rows-to-remove))
                                               (first rows-to-remove)))])))

; END OF GAME

; game-over? : World -> Boolean
; Is the game over?
(define (game-over? w)
  (and (landed? w)
       (not (blocks-in-bounds? (tetra-blocks (world-tetra w))))))

; display-score : World -> Image
; Display the score
(define (display-score w)
  (overlay (above (text "Game Over" 30 "black")
                  (text (string-append "Score: "
                                       (number->string (world-score w)))
                        30 "black")) SCENE))

; main : Any -> Animation
; Start the Bin-Packing Game
(define (main a)
  (big-bang (make-world (new-tetra (random 7))
                        empty
                        0)
            (on-tick next-world .2)
            (to-draw world->scene)
            (on-key key-buffer)
            (stop-when game-over? display-score)))