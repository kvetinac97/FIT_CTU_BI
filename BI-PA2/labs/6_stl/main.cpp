#include <cassert>
#include <iostream>
#include "CSociety.h"

using namespace std;

int main() {
    CPerson db{ "David", "Bernhauer" };
    CPerson jk{ "Jaroslav", "Kriz"};
    CPerson aj{ "Adam", "Jirovsky"};
    CPerson rj{ "Roman", "Jirovsky"};
    CPerson dk{ "David", "Kriz"};
    CPerson dd{ "David", "David"};

    CSociety society;
    {
        try {
            society.GetPerson(db);
            assert("GetPerson should throw an exception");
        } catch (const std::runtime_error & e) {
            std::cout << e.what() << std::endl;
        }
        try {
            society.AddFriendPair(db, jk);
            assert("Should throw an exception, because DB is unknown");
        } catch (const std::runtime_error &e) {
            std::cout << e.what() << std::endl;
        }
        // neat trick to tie assertion with fail message
        assert(society.AddPerson(db) && "DB should have been added");
        assert(!society.AddPerson(db) && "DB should not have been added");
        try {
            society.GetPerson(db);
        } catch (const std::runtime_error & e) {
            assert("Unexpected exception");
        }
        try {
            society.AddFriendPair(db, jk);
            assert("Should throw an exception, because JK is unknown");
        } catch (const std::runtime_error & e) {
            std::cout << e.what() << std::endl;
        }
        assert(society.AddPerson(jk) && "JK should have been added");
        assert(society.AddPerson(aj) && "AJ should have been added");
        assert(society.AddPerson(rj) && "RJ should have been added");
        assert(society.AddPerson(dk) && "DK should have been added");
    }

    {
        assert(society.InfectPerson(aj) == 1 && "Nakazit se ma pouze Adam, protoze nema zadne kamarady");

        // tentokrat konecne spratelime Davida s Jardou, kdyz uz oba existuji
        society.AddFriendPair(db, jk);

        // v ramci trojice se budou vsichni pratelit se vsemi ostatnimi
        society.AddFriendPair(db, rj);
        society.AddFriendPair(jk, rj);

        society.AddFriendPair(rj, dk);

        /* vyobrazeni toho, kdo s kym kamaradi
         *     JK
         *     /_\                                              AJ
         *  DB     RJ - DK
         */

        assert(society.InfectPerson(db) == 4 && "David nakazi i vsechny zbyvajici, i kamarady kamaradu ... protoze David rad stahuje dalsi lidi s sebou ke dnu");

        assert(society.InfectPerson(jk) == 0 && "Jarda uz nikoho dalsiho nenakazi, uz davno vsechny nakazil ... s nim je vzdycky kriz");
    }

    {
        auto infectedList = society.ListInfected("");
        assert(infectedList.size() == 5);
        // David Bernhauer, Jaroslav Kriz, Adam Jirovsky, Roman Jirovsky, David Kriz

        assert(society.AddPerson(dd));
        infectedList = society.ListInfected("Dav");
        assert(infectedList.size() == 2);
        // David Bernhauer, David Kriz

        society.InfectPerson(dd);
        infectedList = society.ListInfected("Dav");
        assert(infectedList.size() == 3);
        // David Bernhauer, David Kriz, David David

        cout << "Success" << endl;
    }
    return 0;
}
