#lang racket

(require rackunit rackunit/text-ui "Matrices-2.rkt" math)

(define matrix-tests
  (test-suite
   "Tests for matrix applications"
   (check-equal? (round-all (rotation-matrix pi)) (matrix [[-1.0 0.0] [-0.0 -1.0]]))
   (check-equal? (round-all (rotation-matrix (/ pi 2))) (matrix [[0.0 1.0] [-1.0 0.0]]))
   (let ([mat1 (row-matrix [1 0])]
         [mat2 (row-matrix [2 0])]
         [mat3 (row-matrix [.5 0])]
         [mat4 (row-matrix [1 1])]
         [mat5 (row-matrix [0 0])])
     (check-equal? (rotate-point mat1 mat5 pi) (row-matrix [-1.0 0.0]))
     (check-equal? (rotate-point mat1 mat2 pi) (row-matrix [3.0 -0.0]))
     (check-equal? (project-point mat1 mat5 mat4) (row-matrix [0 0]))
     (check-equal? (project-point mat1 mat3 mat4) (row-matrix [1.0 0.0]))
     (check-equal? (reflect-point mat1 mat5 mat4) (row-matrix [0 1]))
     (check-equal? (reflect-point mat1 mat3 mat4) (row-matrix [0.0 0.0]))
     (check-equal? (reflect-across-point mat1 mat5) (row-matrix [-1 0])))))

(run-tests matrix-tests)