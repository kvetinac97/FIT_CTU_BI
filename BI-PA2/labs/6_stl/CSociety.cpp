#include <algorithm>
#include <stdexcept>
#include <queue>
#include <iostream>
#include "CSociety.h"

bool CSociety::AddPerson(const CPerson & person) {
    auto it = lower_bound ( m_PersonList.begin(), m_PersonList.end(), person );
    if ( it != m_PersonList.end() && *it == person )
        return false;

    m_PersonList.insert(it, person);
    return true;
}

void  CSociety::AddFriendPair(const CPerson &p1, const CPerson &p2) {
    // Throw exception if they do not exist
    GetPerson(p1);
    GetPerson(p2);

    if ( p1 == p2 )
        return;

    if ( m_Friends.count(p1) == 0 )
        m_Friends.insert( std::make_pair ( p1, std::set<CPerson>() ) );

    if ( m_Friends.count(p2) == 0 )
        m_Friends.insert( std::make_pair ( p2, std::set<CPerson>() ) );

    m_Friends[p1].insert(p2);
    m_Friends[p2].insert(p1);

    if ( m_Infected.count(p1) == 1 )
        m_Infected.insert(p2);

    if ( m_Infected.count(p2) == 1 )
        m_Infected.insert(p1);
}

size_t CSociety::InfectPerson(const CPerson & person) {
    // Infect
    int count = m_Infected.insert(person).second;

    // Infect friends
    std::set<CPerson> people = m_Friends[person];
    for ( auto & it : people ) {
        if ( m_Infected.insert(it).second ) {
            count ++;
            count += InfectPerson(it);
        }
    }
    return count;
}

CPerson & CSociety::GetPerson(const CPerson & person) {
    auto it = lower_bound ( m_PersonList.begin(), m_PersonList.end(), person );
    if ( it == m_PersonList.end() || *it != person )
        throw std::runtime_error("Unknown person " + person.getName());

    return *it;
}

CSociety::InfectedList CSociety::ListInfected(const std::string &prefix) const {

    InfectedList infected;

    for ( auto & it : m_Infected ) {

        if ( it.getFamilyName().substr(0, prefix.length()) == prefix
        ||   it.getGivenName().substr(0, prefix.length()) == prefix)
            infected.insert(it);

    }

    return infected;
}