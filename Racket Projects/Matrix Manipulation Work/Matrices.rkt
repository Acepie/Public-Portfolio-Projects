#lang racket

; a matrix is a list of m lists of size n such that they form an m x n matrix
; for example '((a b c) (d e f)) is a 2 x 3 matrix

(provide (contract-out
          (same-size? (-> matrix? matrix? boolean?))
          (matrix-addition (-> matrix? matrix? matrix?))
          (distance-matrix (-> matrix? matrix? rational?))
          (column->row (-> column? row?))
          (first-column (-> matrix? column?))
          (rest-columns (-> (λ (m) (and (matrix? m)
                                        (not (column? m))))
                            matrix?))
          (transpose (-> matrix? matrix?))
          (row-mult (-> row? row? row?))
          (matrix-multiplication (-> matrix? matrix? matrix?))
          (identity-matrix (-> integer? matrix?))
          (round-by (-> integer? rational? rational?))
          (rotation-matrix (-> rational? matrix?))
          (projection-matrix (-> row? matrix?))
          (rotate-point (-> row? row? rational? row?))
          (reflect-point (-> row? row? row? row?))
          (project-point (-> row? row? row? row?))
          (reflect-across-point (-> row? row? row?))
          (display-line (-> cons? string?))
          (display-matrix (-> matrix? string?))))

;; is this object a matrix
(define (matrix? matrix)
  (and (cons? matrix)
       (cons? (first matrix))
       (andmap (λ (column) (= (length column) (length (first matrix)))) matrix)))

;; are these two matrices the same size
(define (same-size? matrix1 matrix2)
  (and (= (length matrix1) (length matrix2))
       (= (length (first matrix1)) (length (first matrix2)))))

;; is this object a column
(define (column? column)
  (and (matrix? column)
       (= 1 (length (first column)))))

;; is this object a row
(define (row? row)
  (and (matrix? row)
       (= 1 (length row))))

;; convert a column vector into a row vector
(define (column->row column)
  (list (foldr (λ (row sofar) (cons (first row) sofar)) '() column)))

;; find the first column of a matrix
(define (first-column matrix)
  (map (λ (row) (list (first row))) matrix))

;; find the rest of the columns in a matrix
(define (rest-columns matrix)
  (map (λ (row) (rest row)) matrix))

;; transpose a matrix
(define (transpose matrix)
  (cond [(column? matrix) (column->row matrix)]
        [else (append (column->row (first-column matrix)) 
                      (transpose (rest-columns matrix)))]))

;; add 2 matrices
(define (matrix-addition matrix1 matrix2)
  (cond [(not (same-size? matrix1 matrix2)) "matrices not same size"]
        [else (map (λ (row1 row2) (map + row1 row2)) matrix1 matrix2)]))

;;calculate the distance between two points
(define (distance-matrix matrix1 matrix2)
   (cond [(not (same-size? matrix1 matrix2)) "matrices not same size"]
        [else (sqrt (foldr + 0 (map (λ (n1 n2) (sqr (+ n1 n2))) (first matrix1) (first matrix2))))]))

;; calculate the dot product of two rows
(define (row-mult row1 row2)
  (cond [(not (= (length row1) (length row2))) "rows not same length"]
        [else (foldr + 0 (map * (first row1) (first row2)))]))

;; compute matrix multiplication if possible
(define (matrix-multiplication matrix1 matrix2)
  (cond [(not (= (length (first matrix1)) (length matrix2))) "multiplication not possible"]
        [else (map (λ (row) (map (λ (col) (row-mult (list row) (list col))) 
                                 (transpose matrix2))) 
                   matrix1)]))

;; multiply matrix by scalar
(define (scalar-mult matrix scalar)
  (map (λ (row) (map (λ (num) (* num scalar))
                     row))
       matrix))

;; create an identity matrix of size n by n
(define (identity-matrix n)
  (build-list n (λ (num1) (build-list n (λ (num2) (if (= num1 num2)
                                                      1
                                                      0))))))

;; round to n decimal places
(define (round-by n numb)
  (/ (round (* numb (expt 10 n))) (expt 10 n)))

;; find rotation matrix for theta in 2d for row vectors
;; round to 5 decimals
(define (rotation-matrix theta)
  (list (list (round-by 5 (cos theta)) (round-by 5 (sin theta))) 
        (list (round-by 5 (* -1 (sin theta))) (round-by 5 (cos theta)))))

;; find projection matrix for line
;; line is represented as a row vector
(define (projection-matrix line)
  (scalar-mult (matrix-multiplication (transpose line) line)
               (/ 1 (row-mult line line))))

;; find reflection matrix for line
(define (reflection-matrix line)
  (matrix-addition (scalar-mult (projection-matrix line) 2)
                   (scalar-mult (identity-matrix (length (first line))) -1)))

;; round all numbers in matrix to integers
(define (round-all matrix)
  (map (λ (row) (map round
                     row))
       matrix))

;; rotate a point around a center
(define (rotate-point point center theta)
  (round-all (matrix-addition (matrix-multiplication (matrix-addition point (scalar-mult center -1))
                                                     (rotation-matrix theta))
                              center)))

;; reflect a point across a line with direction vector and a point it passes through
(define (reflect-point point passingpoint line)
  (round-all (matrix-addition (matrix-multiplication (matrix-addition point 
                                                                      (scalar-mult passingpoint -1))
                                                     (reflection-matrix line))
                              passingpoint)))

;; project a point onto a line with direction vector and a point it passes through
(define (project-point point passingpoint line)
  (round-all (matrix-addition (matrix-multiplication (matrix-addition point 
                                                                      (scalar-mult passingpoint -1))
                                                     (projection-matrix line))
                              passingpoint)))

;; create a line perpendicular to this line in 2D
(define (perpendicular line)
  (list (list (second (first line)) (* (first (first line)) -1))))

;; reflect a point across anothe point in 2D
(define (reflect-across-point point reflection-point)
  (reflect-point point 
                 reflection-point
                 (perpendicular (matrix-addition point (scalar-mult reflection-point -1)))))

;; turn a display matrix into a standard matrix
(define (display-to-standard matrixstring)
  (map (λ (row) (map (λ (num) (- (char->integer num) 48))
                     (string->list (string-replace (string-trim row "|") " " ""))))
       (string-split matrixstring "\n")))

;; display 1 line of a matrix
(define (display-line line)
  (cond [(empty? (rest line)) (~a (first line))]
        [else (string-append (~a (first line)) " " (display-line (rest line)))]))

;; display a matrix
(define (display-matrix matrix)
  (foldr (λ (row rst) (string-append "|"
                                     (display-line row)
                                     "|"
                                     "\n"
                                     rst))
         ""
         matrix))

;; combine strings in list into a single string for displaying
(define (display-all lst)
  (display (foldr (λ (piece rst) (string-append (display-matrix piece) "\n" rst)) "" lst)))


;; display
(display-all (list '((14 14) (14 14))
                   '((1 1) (2 2) (3 3))
                   '((1 0) (0 1))
                   '((1 2 3))
                   '((1) (2) (3))
                   (rotation-matrix pi)
                   (rotation-matrix (/ pi 2))
                   (projection-matrix '((1 1)))
                   (reflection-matrix '((1 1)))
                   (identity-matrix 3)
                   (rotate-point '((1 0)) '((0 0)) pi)
                   (rotate-point '((1 0)) '((2 0)) pi)
                   (project-point '((1 0)) '((0 0)) '((1 1)))
                   (project-point '((1 0)) '((.5 0)) '((1 1)))
                   (reflect-point '((1 0)) '((0 0)) '((1 1)))
                   (reflect-point '((1 0)) '((.5 0)) '((1 1)))
                   (reflect-across-point '((1 0)) '((0 0)))))