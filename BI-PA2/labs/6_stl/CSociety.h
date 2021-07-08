#pragma once

#include <map>
#include <set>
#include "CPerson.h"

class CSociety {
        using InfectedList = std::set<CPerson>;
        using FriendPairs  = std::map<CPerson,std::set<CPerson>>;
    public:
        bool         AddPerson    (const CPerson & person);
        void         AddFriendPair(const CPerson & p1, const CPerson & p2);
        size_t       InfectPerson (const CPerson & person);
        InfectedList ListInfected (const std::string & prefix) const;
        CPerson &    GetPerson    (const CPerson & person);
    private:
        std::vector<CPerson> m_PersonList;
        std::set<CPerson>      m_Infected;
        FriendPairs             m_Friends;
};
