% Seřazení pole

%insert(+E, +Lst, -Res).

insert(E, [], [E]) :- !.
insert(E, [H|T], [E, H | T]) :-
    E < H, !.
insert(E, [H|T], [H|Res]) :-
    E >= H,
    insert(E, T, Res).

insert_sort(Lst, Res) :-
    insert_sort(Lst, [], Res) .

insert_sort([], Sorted, Sorted).

insert_sort([H|T], Sorted, Res) :-
    insert(H, Sorted, SortedWithH),
    insert_sort(T, SortedWithH, Res).

% ----------------------------------

% my_len(+Lst, -LstLen)
my_len([], 0).
my_len([_|T], Len) :-
    my_len(T, LenSh),
    Len is LenSh + 1.

% my_appendList(+R1, +R2, -Lst)
my_appendList([], L2, L2).
my_appendList([H|T], L2, [H|Res]) :-
    my_appendList(T, L2, Res).

% halve(+Lst, -R1, -R2)

halve(Lst, R1, R2) :-
    my_len(Lst, LstLen),
    R1Len is LstLen // 2,
    R2Len is LstLen - R1Len,
    my_len(R1, R1Len),
    my_len(R2, R2Len),
    !,
    my_appendList(R1, R2, Lst).

% ----------------------------------

% Máme tři nádoby, 8, 5 a 3 litrů.
% Chceme odměřit tak, abychom měli 4 a 4 litry.

% Přeléváme z 1 do 2
move(V1/W1, V2/W2, V1/Wn1, V2/Wn2) :-
    W1 > 0,
    AvailableIn2 is V2 - W2,
    W1 > AvailableIn2, % nastal overflow
    Wn2 = V2,
    Wn1 is W1 - AvailableIn2.

move(V1/W1, V2/W2, V1/Wn1, V2/Wn2) :-
    W1 > 0,
    AvailableIn2 is V2 - W2,
    W1 =< AvailableIn2,
    Wn2 is W2 + W1,
    Wn1 = 0.

gen_move([C1, C2, C3], [N1, N2, C3]) :- move(C1, C2, N1, N2).
gen_move([C1, C2, C3], [N1, C2, N3]) :- move(C1, C3, N1, N3).
gen_move([C1, C2, C3], [N1, N2, C3]) :- move(C2, C1, N2, N1).
gen_move([C1, C2, C3], [C1, N2, N3]) :- move(C2, C3, N2, N3).
gen_move([C1, C2, C3], [N1, C2, N3]) :- move(C3, C1, N3, N1).
gen_move([C1, C2, C3], [C1, N2, N3]) :- move(C3, C2, N3, N2).

not_member(E, Lst) :- member(E, Lst), !, fail.
not_member(_, _).

solve(State, Target, Result) :-
    solve(State, [State], Target, Result). % Druhý argument = seznam navštívených stavů

solve(State, Path, State, Path).

solve(State, Visited, Target, Result) :-
    gen_move(State, NewState),
    not_member(NewState, Visited),
    solve(NewState, [NewState|Visited], Target, Result).

