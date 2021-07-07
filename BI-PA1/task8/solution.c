#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#define LIST_BY_YEAR       0
#define LIST_BY_TYPE       1

#define TYPE_MAX           100
#define SETUP_MAX          100

typedef struct TEngine
{
  struct TEngine * m_Next;
  struct TEngine * m_Prev;
  int              m_Year;
  char             m_Type  [ TYPE_MAX ];
  int              m_Setup [ SETUP_MAX ];
} TENGINE;

typedef struct TArchive
{
  struct TArchive * m_Next;
  struct TArchive * m_Prev;
  TENGINE         * m_Engines;
} TARCHIVE;

TENGINE          * createEngine                            ( const char      * type,
                                                             int               year )
{
  TENGINE * res = (TENGINE *) malloc ( sizeof  (*res ) );
  res -> m_Next = NULL;
  res -> m_Prev = NULL;
  res -> m_Year = year;
  strncpy ( res -> m_Type, type, sizeof ( res -> m_Type ) );
  for ( int i = 0; i < SETUP_MAX; i ++ )
    res -> m_Setup[i] = 0;
  return res;
}

int AddEngineIntoArchive ( TARCHIVE * list, int listBy, TENGINE * engine ) {
    
    TENGINE * startElement = list->m_Engines;
    
    TENGINE * engineIter = startElement, * engineOrder = NULL;
    int orderMatch = 0;
    
    do {
        
        if ( listBy == LIST_BY_YEAR )
            orderMatch = ( engineIter->m_Year < engine->m_Year );
        
        else if ( listBy == LIST_BY_TYPE )
            orderMatch = ( strncmp ( engineIter->m_Type, engine->m_Type, TYPE_MAX ) < 0 );
        
        else
            orderMatch = 0;
        
        if ( orderMatch )
            engineOrder = engineIter;
                
    }
    while ( ( engineIter = engineIter->m_Next ) );
    
    //Prepend at the beggining
    if ( !engineOrder ) {
        
        engine->m_Prev = NULL;
        engine->m_Next = startElement;
        startElement->m_Prev = engine;
        
        list->m_Engines = engine;
        return 1;
        
    }
            
   engine->m_Prev = engineOrder;
   engine->m_Next = engineOrder->m_Next;
   
   if ( engineOrder->m_Next )
       engineOrder->m_Next->m_Prev = engine;
   
   engineOrder->m_Next = engine;
    
    //Repair the engine pointer
    while ( engine->m_Prev )
        engine = engine->m_Prev;
    
    list->m_Engines = engine;
    return 1;
    
}

TARCHIVE         * AddEngine                               ( TARCHIVE        * list,
                                                             int               listBy,
                                                             TENGINE         * engine )
{
      
    //First archive
    if ( list == NULL ) {
        
        TARCHIVE * listEl = (TARCHIVE *) malloc ( sizeof (*listEl) ); //TODO free
        
        listEl->m_Prev = NULL;
        listEl->m_Next = NULL;
        listEl->m_Engines = engine;
        
        return listEl;
        
    }
        
    TARCHIVE * listIter = list, * listFound = NULL, * listOrder = NULL;
    int orderMatch = 0;
    
    //Same year / type
    do {
                
        if ( listBy == LIST_BY_YEAR && engine->m_Year == listIter->m_Engines->m_Year ) {
            
            AddEngineIntoArchive ( listIter, LIST_BY_TYPE, engine );
            listFound = listIter;
            continue;
            
        }
        
        if ( listBy == LIST_BY_TYPE && strncmp ( engine->m_Type, listIter->m_Engines->m_Type, TYPE_MAX ) == 0 ) {
            
            AddEngineIntoArchive ( listIter, LIST_BY_YEAR, engine );
            listFound = listIter;
            continue;
            
        }
        
        if ( listBy == LIST_BY_YEAR )
            orderMatch = ( listIter->m_Engines->m_Year < engine->m_Year );
        
        else if ( listBy == LIST_BY_TYPE )
            orderMatch = ( strncmp ( listIter->m_Engines->m_Type, engine->m_Type, TYPE_MAX ) < 0 );
        
        else
            orderMatch = 0;
        
        if ( orderMatch )
            listOrder = listIter;
                
    }
    while ( ( listIter = listIter->m_Next ) );
    
    if ( listFound ) {
        
        //Repair the list pointer
        while ( listFound->m_Prev )
            listFound = listFound->m_Prev;
        
        return listFound;
        
    }
    
    // New archive
    TARCHIVE * listEl = (TARCHIVE *) malloc ( sizeof (*listEl) ); //TODO free
    listEl->m_Engines = engine;
    
    // Prepend at start
    if ( !listOrder ) {
        
        listEl->m_Prev = NULL;
        listEl->m_Next = list;
        list->m_Prev = listEl;
        
        return listEl;
        
    }
    
    //Insert the new archive after this element
    listEl->m_Prev = listOrder;
    listEl->m_Next = listOrder->m_Next;
    
    if ( listOrder->m_Next )
        listOrder->m_Next->m_Prev = listEl;
    
    listOrder->m_Next = listEl;
    
    //Repair the list pointer
    while ( list->m_Prev )
        list = list->m_Prev;
    
    return list;
    
}
void               DelArchive                              ( TARCHIVE        * list )
{
    
    if ( list == NULL )
        return;
  
    TARCHIVE * archive = list, * archiveNext = archive->m_Next;
    
    do {
        
        TENGINE * engine = archive->m_Engines, * engineNext = engine->m_Next;
        do {
            
            free ( engine );
            engine = engineNext;
            
            if ( engine )
                engineNext = engine->m_Next;
            else
                engineNext = NULL;
            
        }
        while ( engine );
        
        free ( archive );
        
        archive = archiveNext;
        
        if ( archive )
            archiveNext = archive->m_Next;
        else
            archiveNext = NULL;
                
    }
    while ( archive );
    
}
TARCHIVE         * ReorderArchive                          ( TARCHIVE        * list,
                                                             int               listBy )
{
    
    if ( list == NULL )
        return NULL;
    
    TARCHIVE * archive = list;
    int engineCount = 0;
    
    do {
        
        TENGINE * engine = archive->m_Engines;
        
        do
            engineCount++;
        while ( ( engine = engine->m_Next ) );
                
    }
    while ( ( archive = archive->m_Next ) );
    
    archive = list;
    TENGINE ** engines = ( TENGINE ** ) malloc ( ( engineCount + 1 ) * sizeof ( *engines ) );
    
    engineCount = 0;
    
    do {
        
        TENGINE * engine = archive->m_Engines;
        
        do
            engines[engineCount++] = engine;
        while ( ( engine = engine->m_Next ) );
        
        if ( archive->m_Next ) {
            
            archive = archive->m_Next;
            free ( archive->m_Prev );
            
        }
        
        else {
            
            free ( archive );
            archive = NULL;
            
        }
                        
    }
    while ( archive );
    
    TARCHIVE * arch = NULL;
    
    for ( int i = 0; i < engineCount; i++ ) {
        engines[i]->m_Prev = NULL;
        engines[i]->m_Next = NULL;
        
        arch = AddEngine ( arch, listBy, engines[i] );
    }
    
    free ( engines );
    
    return arch;
    
}
