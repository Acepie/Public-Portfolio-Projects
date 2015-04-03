#lang racket

(define (page-parser current last around)
  (cond [(or (< around 0)
             (< last 1)
             (< (- current around) 1)
             (> (+ current around) last))
         "ERR"]
        [(< (+ 2 around) current (- last 1 around)) 
         (string-append "1 ... " (nums-around current around) " ... " (~a last))]
        [(and (<= (- last 2) (add1 (* 2 around)))
              (= current (/ (add1 last) 2)))
         (nums-until last)]
        [(<= (- current around) 2) 
         (string-append (nums-until (add1 current)) " ... " (~a last))]
        [(>= (+ current around) (sub1 last)) 
         (string-append "1 ..." (nums-from (sub1 current) last))]))

(define (nums-around current around)
  (nums-from (- current around) (+ current around)))

(define (nums-until end)
  (nums-from 1 end))

(define (nums-from start end)
  (if (= end start)
      (~a start)
      (string-append (nums-from start (sub1 end)) " " (~a end))))

(page-parser 7 10 1)
(page-parser 4 10 1)
(page-parser 3 10 1)
(page-parser 8 10 1)
(page-parser 2 5 1)
(page-parser 5 7 2)
(page-parser 3 5 1)
(page-parser 4 7 2)
(page-parser 9 10 1)
(page-parser 10 10 1)
(page-parser 1 10 1)
(page-parser 1 1 0)

