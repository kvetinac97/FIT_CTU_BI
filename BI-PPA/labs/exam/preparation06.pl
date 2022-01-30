pridej(V, [], [V, [], []]) :- !.
pridej(X, [V, L, R], [V, LRes, R]) :- X < V, !, pridej(X, L, LRes).
pridej(X, [V, L, R], [V, L, RRes]) :- X > V, !, pridej(X, R, RRes).

vyrobStrom(Lst, Res) :- reverse(Lst, Rev), vyrobStromAux(Rev, Res).
vyrobStromAux([], []) :- !.
vyrobStromAux([H|T], Res) :- vyrobStromAux(T, Tree), pridej(H, Tree, Res).

reverse(Lst, Rev) :- reverse(Lst, [], Rev).
reverse([], Acc, Acc).
reverse([H|T], Acc, Res) :- reverse(T, [H|Acc], Res).

spoj([], Lst, Lst) :- !.
spoj([H|T], Lst, [H|Res]) :- spoj(T, Lst, Res).

tiskniStrom([], []).
tiskniStrom([V, L, R], Res) :- tiskniStrom(L, LRes), tiskniStrom(R, RRes), spoj(LRes, [V|RRes], Res).

serad(Lst, Res) :- vyrobStrom(Lst, Tree), tiskniStrom(Tree, Res).