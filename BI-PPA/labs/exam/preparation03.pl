% printFirstColumn(+Matrix, -Res).
printFirstColumn([], []).
printFirstColumn([[First|_]|Rows], [First|Res]) :- printFirstColumn(Rows, Res).

% removeFirstColumn(+Matrix, -Res).
removeFirstColumn([], []).
removeFirstColumn([[_|Cols]|Rows], [Cols|Res]) :- removeFirstColumn(Rows, Res).

% transpose(+Matrix, -Res).
transpose([], []).
transpose([[]|T], []) :- transpose(T, []), !.
transpose(Matrix, [H|T]) :- printFirstColumn(Matrix, H),
    removeFirstColumn(Matrix, Rem), transpose(Rem, T).


