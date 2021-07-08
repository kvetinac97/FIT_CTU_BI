#include "CPerson.h"

bool CPerson::operator < (const CPerson & student) const {
    if ( m_FamilyName != student.m_FamilyName )
        return m_FamilyName < student.m_FamilyName;

    return m_GivenName < student.m_GivenName;
}
bool CPerson::operator == (const CPerson & student) const {
    return !(*this < student) && !(student < *this);
}
bool CPerson::operator > (const CPerson & student) const {
    return student < *this;
}
bool CPerson::operator !=(const CPerson & student) const {
    return !(student == *this);
}
bool CPerson::operator <= (const CPerson & student) const {
    return !(*this < student);
}
bool CPerson::operator >= (const CPerson & student) const {
    return !(student < *this);
}

bool CPerson::IsInfected() const {
    return m_Infected;
}
void CPerson::Infect() {
    m_Infected = true;
}
std::string CPerson::getName() const {
    return m_GivenName + " " + m_FamilyName;
}