#pragma once

#include <memory>
#include <string>
#include <tuple>
#include <utility>
#include <vector>

class CPerson {
    public:
        CPerson(std::string  givenName, std::string  familyName) : m_GivenName(std::move(givenName)), m_FamilyName(std::move(familyName)) { }
        bool operator< (const CPerson & student) const;
        bool operator==(const CPerson & student) const;
        bool operator> (const CPerson & student) const;
        bool operator!=(const CPerson & student) const;
        bool operator<= (const CPerson & student) const;
        bool operator>= (const CPerson & student) const;
        bool IsInfected() const;
        void Infect();
        std::string getName() const;
        std::string getGivenName() const { return m_GivenName; }
        std::string getFamilyName() const { return m_FamilyName; }
    private:
        std::string m_GivenName;
        std::string m_FamilyName;
        bool        m_Infected = false;
};
