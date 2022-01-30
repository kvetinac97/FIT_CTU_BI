% ins(+X, +List, -Res)
ins(X, [], [X]) :- !.
ins(X, [H|T], [X,H|T]) :- X =< H, !.
ins(X, [H|T], [H|Res]) :- X > H, ins(X, T, Res).

% insort(+List, -Res)
insort([], []).
insort([H|T], Res) :- insort(T, R), ins(H, R, Res).


