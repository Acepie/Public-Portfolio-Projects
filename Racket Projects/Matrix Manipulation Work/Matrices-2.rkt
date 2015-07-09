#lang racket

(require math)

(provide (contract-out (distance-matrix (-> row-matrix? row-matrix? rational?))
                       (rotation-matrix (-> rational? matrix?))
                       (projection-matrix (-> row-matrix? matrix?))
                       (reflection-matrix (-> row-matrix? matrix?))
                       (rotate-point (-> row-matrix? row-matrix? rational? row-matrix?))
                       (reflect-point (-> row-matrix? row-matrix? row-matrix? row-matrix?))
                       (project-point (-> row-matrix? row-matrix? row-matrix? row-matrix?))
                       (reflect-across-point (-> row-matrix? row-matrix? row-matrix?))
                       (round-all (-> matrix? matrix?))))

;;calculate the distance between two points
(define (distance-matrix matrix1 matrix2)
  (cond [(not (equal? (matrix-num-cols matrix1) (matrix-num-cols matrix2))) "matrices not same size"]
        [else (sqrt (matrix-dot (matrix-map - matrix1 matrix2)))]))

;; find rotation matrix for theta in 2d for row vectors
(define (rotation-matrix theta)
  (matrix [[(cos theta) (sin theta)] [(* -1 (sin theta)) (cos theta)]]))

;; find projection matrix for line
;; line is represented as a row matrix
(define (projection-matrix line)
  (matrix-scale (matrix* (matrix-transpose line) line)
                (/ 1 (matrix-dot line))))

;; find reflection matrix for line
;; line is represented as a row matrix
(define (reflection-matrix line)
  (matrix+ (matrix-scale (projection-matrix line) 2)
           (matrix-scale (identity-matrix (matrix-num-cols line)) -1)))

;; round all numbers in matrix to integers
(define (round-all matrix)
  (matrix-map round matrix))

;; rotate a point around a center
(define (rotate-point point center theta)
  (round-all (matrix+ (matrix* (matrix+ point (matrix-scale center -1))
                               (rotation-matrix theta))
                      center)))

;; reflect a point across a line with direction vector and a point it passes through
(define (reflect-point point passingpoint line)
  (round-all (matrix+ (matrix* (matrix+ point 
                                        (matrix-scale passingpoint -1))
                               (reflection-matrix line))
                      passingpoint)))

;; project a point onto a line with direction vector and a point it passes through
(define (project-point point passingpoint line)
  (round-all (matrix+ (matrix* (matrix+ point 
                                        (matrix-scale passingpoint -1))
                               (projection-matrix line))
                      passingpoint)))

;; create a line perpendicular to this line in 2D
(define (perpendicular line)
  (row-matrix [(matrix-ref line 0 1) (matrix-ref line 0 0)]))

;; reflect a point across anothe point in 2D
(define (reflect-across-point point reflection-point)
  (reflect-point point 
                 reflection-point
                 (perpendicular (matrix+ point (matrix-scale reflection-point -1)))))