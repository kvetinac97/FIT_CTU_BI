#include "CDouble.hpp"

double CDouble::get () const {
    return nextafter ( m_Prev, m_Next );
}
