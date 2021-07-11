// The classes in this header are used in the example test. You are free to 
// modify these classes, add more test cases, and add more test sets.
// These classes do not exist in the progtest's testing environment.
#ifndef SAMPLE_TESTER_H_234789561294356297
#define SAMPLE_TESTER_H_234789561294356297

#include <map>
#include "common.h"

//=================================================================================================
/**
 * An example CCustomer implementation suitable for testing. This implementation does not cause 
 * any delays in Quote(), add other CCustomer implementation to test correct handling of the delays.
 */
class CCustomerTest : public CCustomer
{
  public:
    //---------------------------------------------------------------------------------------------
    /**
     * A helper method to prepare the test instance. This method is not present in CCustomer interface.
     *
     * @param[in] destination  the destination of the cargo being added
     * @param[in] cargo        the cargo to add 
     */ 
    void                     Add                           ( const std::string    & destination,
                                                             const CCargo         & cargo );
    //---------------------------------------------------------------------------------------------
    /**
     * A basic implementation of the Quote method from the base class. Just returns the cargo list for 
     * the destination in parameter.
     *
     * @param[in] destination  the destination to quote
     * @param[out] cargo       the list of cargo for the destination
     */
    virtual void             Quote                         ( const std::string    & destination,
                                                             std::vector<CCargo>  & cargo ) override;
  private:
    std::map<std::string, std::vector<CCargo> > m_Data;
};
typedef std::shared_ptr<CCustomerTest>                     ACustomerTest;
//=================================================================================================
/**
 * An example CShip implementation suitable for testing. 
 */ 
class CShipTest : public CShip
{
  public:
    //---------------------------------------------------------------------------------------------
    /**
     * Constructor - initialize the fields. 
     *
     * @param[in] destination   ship's destination
     * @param[in] maxWeight     ship's capacity 
     * @param[in] maxVolume     ship's capacity   
     * @param[in] expected      the expected result, the sum of fees of the loded cargo will be compared with this value
     */
                             CShipTest                     ( std::string             destination,
                                                             int                     maxWeight,
                                                             int                     maxVolume,
                                                             int                     expected );
    //---------------------------------------------------------------------------------------------
    /**
     * Load the ship - save the list of cargo
     * @param[in] cargo         the cargo list to store
     */
    virtual void             Load                          ( const std::vector<CCargo>  & cargo ) override;
    //---------------------------------------------------------------------------------------------
    /**
     * A simlpe test method - validate the cargo list (previously loaded with the Load method), i.e., compare 
     * the sum of the fees with the expected value.
     * @return true if the result matches, false otherwise
     * @note this method is present in this implementation, however, the method is not present in the base class CShip
     */ 
    bool                     Validate                      ( void ) const;
  private:
    int                      m_Expected;
    std::vector<CCargo>      m_Load;
};
typedef std::shared_ptr<CShipTest>                         AShipTest;
//=================================================================================================
/**
 * A simlpe class that encapsulates one set of test data and the expected result. The class is present 
 * for the testing only, it does not exist in the progtest testing environment.
 */
class CSampleData
{
  public:
    //---------------------------------------------------------------------------------------------
    /**
     * Constructor - set the fields
     * @param[in] expected   the expected sum of fees
     * @param[in] maxWeight  ship's capacity 
     * @param[in] maxVolume  ship's capacity   
     * @param[in] cargo      the cargo to process, i.e., the loaded cargo is chosen from this list
     */
                             CSampleData                   ( int                     expected,
                                                             int                     maxWeight,
                                                             int                     maxVolume,
                                                             std::initializer_list<CCargo> cargo )
      : m_Expected ( expected ),
        m_MaxWeight ( maxWeight ),
        m_MaxVolume ( maxVolume ),
        m_Cargo ( cargo )
    {
    }
    //---------------------------------------------------------------------------------------------
    /**
     * A helper method to fill-in ship and customer instances with the data from one single testset.
     * @param[in] destination  the destination to fill into the customers and ship 
     * @param     customers    the cargo list is split and randomly filled into the customers in this array
     * @return a new ship instance filled with the expected result
     */
    AShipTest                PrepareTest                   ( std::string       destination,
                                                             std::vector<ACustomerTest> customers ) const;
  private:  
    int                      m_Expected;
    int                      m_MaxWeight;
    int                      m_MaxVolume;
    std::vector<CCargo>      m_Cargo;
};
//=================================================================================================

extern std::vector<CSampleData> g_TestExtra;

#endif /* SAMPLE_TESTER_H_234789561294356297 */
