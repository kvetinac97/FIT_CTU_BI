%my_length(+Lst, -Len)
my_length([], 0).
my_length([_|Xs], Len) :- my_length(Xs, LenRest), Len is LenRest + 1.

%my_member(+Lst, +E)
my_member([], _) :- fail.
my_member([H|_], H).
my_member([H|T], E) :- E \= H, my_member(T, E).

