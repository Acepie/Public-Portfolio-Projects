#lang racket

(define (numfst lst)
  (cond [(empty? lst) 0]
        [(empty? (rest lst)) 1]
        [(char=? (first lst) (first (rest lst))) (+ 1 (numfst (rest lst)))]
        [else 1]))

(define (removefirstn lst n)
  (cond [(= n 0) lst]
        [else (removefirstn (rest lst) (sub1 n))]))

(define (compress lst)
  (cond [(empty? lst) lst]
        [(= (numfst lst) 1) (cons (first lst) (compress (rest lst)))]
        [else (cons (~a (numfst lst)) 
                    (cons (first lst) (compress (removefirstn lst (numfst lst)))))]
        ))

(define (lst->str lst)
  (cond [(empty? lst) ""]
        [(string? (first lst)) (string-append (first lst) (lst->str (rest lst)))]
        [else (string-append (make-string 1 (first lst)) (lst->str (rest lst)))]))

(define (compressor str)
  (lst->str (compress (string->list str))))

(define (compressall input)
    (compressallhelp (string-split input)))

(define (compressallhelp lst)
  (if (empty? lst)
        ""
        (string-append (compressor (first lst)) "\n" (compressallhelp (rest lst)))))

(display (compressall "uuuudhifffffffBBBBBBBBLLxxx
fheigusya
VVVVVVVVVVVVVVVVVVVVVVVVVVVVV
wwwWWwwwwwWWWW
ddddddthisiscooluuuuuuuuuuuu
vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvVVVVVVVVVVVVVVVVVVVVVVVvvvV
thisisawholesentencethatdoesnotencodewell
kkkuuugggrrrUUUFFFLLLvvvSSSQQQpppIIIjjj
jHHHhhhhhde
nnnnnmdfnnnn
hhhpjppphjppphpjjjhppphhhjppp
J
abcdefghijklmmnopqrstuvwxyz
GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxXxxx
mmmmmmmmmmmmmmmmMmmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"))
    