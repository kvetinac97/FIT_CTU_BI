%arity_of (+FnArityList, +FnName, -Arity)
arity_of([[F|[Arity]]|_], F, Arity) :- !.
arity_of([_|T], F, Arity) :- arity_of(T, F, Arity).

%my_len (+List, -Len)
my_len([], 0).
my_len([_|T], X) :- my_len(T, A), X is A + 1.

%arity_check (+Calls, +FnArityList)
arity_check([], _) :- !.
arity_check([[F|Args]|Next], ArityList) :- arity_of(ArityList, F, Arity),
    my_len(Args, Arity), arity_check(Next, ArityList), !.

