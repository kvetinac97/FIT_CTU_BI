% myMax(+Lst, -Max)
myMax([H], H) :- !.
myMax([H|T], H) :- myMax(T, TMax), TMax =< H, !.
myMax([H|T], TMax) :- myMax(T, TMax), TMax > H, !.

% myRem(+Lst, +X, -Res)
myRem([], _, []).
myRem([H|T], H, T) :- !.
myRem([H|T], X, [H|R]) :- X \= H, myRem(T, X, R).

% selsort(+Lst, -Res)
selSort([], []) :- !.
selSort(Lst, [Max|Res]) :- myMax(Lst, Max),
    myRem(Lst, Max, Rem), selSort(Rem, Res) .
