bigger(dog, mouse).
bigger(cow, worm).
bigger(dog, worm).
bigger(elephant, dog).
bigger(whale, elephant).

%základní fakt
is_bigger(X, Y) :- bigger(X, Y).

is_bigger(X, Z) :- bigger(X, Y), is_bigger(Y, Z).

% Je přímka svislá? Pokud má stejné první souřadnice
% Y1 a Y2 must být rozdílné
vertical(segment(point(X,Y1), point(X,Y2))) :- Y1 \= Y2 .

% Co takový faktoriál
fact(0, 1) .
fact(N, Res) :-
  N1 is N - 1,
  fact(N1, SubRes),
  Res is N * SubRes .