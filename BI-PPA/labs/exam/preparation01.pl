% bst_insert (+E, +T, -R).
bst_insert(X, nil, bst(X, nil, nil)).
bst_insert(X, bst(V, L, R), bst(V, Res, R)) :- X < V ,
  bst_insert(X, L, Res), !.
bst_insert(X, bst(V, L, R), bst(V, L, Res)) :- X > V ,
  bst_insert(X, R, Res), !. 

% bst_create (+L, -T).
bst_create(L, T) :- bst_create_aux(L, nil, T).

% bst_create (+L, +Tree, -T).
bst_create_aux([], Tree, Tree).
bst_create_aux([H|T], Tree, Res) :- bst_insert(H, Tree, R),
  bst_create_aux(T, R, Res).

% list_reverse (+L, -R).
list_reverse(L, R) :- list_reverse(L, [], R).

% list_reverse (+L, +Aux, -R).
list_reverse([], Aux, Aux).
list_reverse([H|T], Aux, R) :- list_reverse(T, [H|Aux], R).

bst_create_2(L, T) :- list_reverse(L, LRev), bst_create_aux(LRev, nil, T).

% bst_inorder (+T, -L)
bst_inorder(nil, []).
bst_inorder(bst(V, L, R), Res) :- bst_inorder(L, LRes), bst_inorder(R, RRes), my_append(LRes, [V], RRes, Res).

my_append(F,S,T,O) :- my_append(F,S,A), my_append(A,T,O) .
my_append([],S,S).
my_append([H|T],S,[H|Res]) :- my_append(T,S,Res).

% treesort (+L, -R).
treesort(L,R) :- bst_create(L,T), bst_inorder(T,R).


