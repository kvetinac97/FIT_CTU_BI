// The classes in this header define the common interface between your implementation and 
// the testing environment. Exactly the same implementation is present in the progtest's 
// testing environment. You are not supposed to modify any declaration in this file, 
// any change is likely to break the compilation.
#ifndef COMMON_H_29034590234651236421345768912
#define COMMON_H_29034590234651236421345768912

#include <cstdint>
#include <memory>
#include <string>
#include <vector>

//=================================================================================================
class CCargo
{
  public:
                   CCargo                                  ( int               fee,
                                                             int               weight,
                                                             int               volume )
      : m_Fee ( fee ),
        m_Weight ( weight ),
        m_Volume ( volume )
    {
    }
    
    int m_Fee;
    int m_Weight;
    int m_Volume;
};
//=================================================================================================
class CShip
{
  public:
                             CShip                         ( std::string             destination,
                                                             int                     maxWeight,
                                                             int                     maxVolume )
      : m_Destination ( move ( destination ) ),
        m_MaxWeight ( maxWeight ),
        m_MaxVolume ( maxVolume )
    {
      
    }
    //---------------------------------------------------------------------------------------------
    virtual                  ~CShip                        ( void ) = default;
    //---------------------------------------------------------------------------------------------
    const std::string      & Destination                   ( void ) const
    {
      return m_Destination;
    }
    //---------------------------------------------------------------------------------------------
    int                      MaxWeight                     ( void ) const
    {
      return m_MaxWeight;
    }
    //---------------------------------------------------------------------------------------------
    int                      MaxVolume                     ( void ) const
    {
      return m_MaxVolume;
    }
    //---------------------------------------------------------------------------------------------
    virtual void             Load                          ( const std::vector<CCargo>  & cargo ) = 0;
    //---------------------------------------------------------------------------------------------
  private:
    std::string              m_Destination;
    int                      m_MaxWeight;
    int                      m_MaxVolume;
};
typedef std::shared_ptr<CShip>    AShip;
//=================================================================================================
class CCustomer
{
  public:
    virtual                  ~CCustomer                    ( void ) = default;
    virtual void             Quote                         ( const std::string    & destination,
                                                             std::vector<CCargo>  & cargo ) = 0;
};
typedef std::shared_ptr<CCustomer> ACustomer;
//=================================================================================================
#endif /* COMMON_H_29034590234651236421345768912 */
