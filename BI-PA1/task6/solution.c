#include<ctype.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>

typedef struct TSentence {
    double frequence;
    char * sentence;
    char * sentenceLower;
    int sentenceLength;
} TSENTENCE;

int loadSentences ( TSENTENCE ** sentences, int * sentenceCount );
int sentenceCmp ( TSENTENCE * s1, TSENTENCE * s2 );
void searchPhrases ( TSENTENCE * sentences, int sentenceCount );
int matchesPhrase ( char * sentence, int sentenceLength, char * phrase, int phraseLength );

int main(void){
    
    TSENTENCE * sentences = NULL;
    int sentenceCount = 0;
    
    printf("Casto hledane fraze:\n");
    
    if ( !loadSentences( &sentences, &sentenceCount ) || sentenceCount == 0 ) {
        printf("Nespravny vstup.\n");
        return 1;
    }
    
    qsort ( sentences, sentenceCount, sizeof( *sentences ), ( int (*) ( const void *, const void * ) ) sentenceCmp );
    
    printf("Hledani:\n");
    searchPhrases( sentences, sentenceCount );
    
    for ( int i = 0; i < sentenceCount; i++ ) {
        free ( sentences[i].sentence );
        free ( sentences[i].sentenceLower );
    }
    
    free ( sentences );
    
    return 0;
    
}

int loadSentences ( TSENTENCE ** sentences, int * sentenceCount ) {
    
    TSENTENCE sentence, * tmpSentences = NULL;
    char delimiter, * tmpSentence = NULL;
    size_t tmpSentenceSize;
    int loadCharacters, stripChars = 0, sentenceCnt = 0, maxSentenceCnt = 0;
    
    while ( 1 ) {
        
        loadCharacters = getline ( &tmpSentence, &tmpSentenceSize, stdin );
        
        if (loadCharacters == EOF) {
            
            for ( int i = 0; i < sentenceCnt; i++ ) {
                free ( tmpSentences[i].sentence );
                free ( tmpSentences[i].sentenceLower );
            }
            
            free ( tmpSentence );
            free ( tmpSentences );
            
            return 0;
            
        }
        
        //Empty row, time to peacefully exit
        if ( loadCharacters == 1 && tmpSentence[0] == '\n' )
            break;
        
        //Strip newline
        if ( tmpSentence[ strlen( tmpSentence ) - 1 ] == '\n' )
            tmpSentence[ strlen( tmpSentence ) - 1] = '\0';
        
        if ( ( sscanf ( tmpSentence, "%lf%c%n", &sentence.frequence, &delimiter, &stripChars ) ) != 2 ||
            delimiter != ':' ) {
                        
            for ( int i = 0; i < sentenceCnt; i++ ) {
                free ( tmpSentences[i].sentence );
                free ( tmpSentences[i].sentenceLower );
            }
            
            free ( tmpSentence );
            free ( tmpSentences );
            
            return 0;
        }
        
        if ( sentenceCnt >= maxSentenceCnt ) {
            maxSentenceCnt += ( maxSentenceCnt < 8 ? 64 : ( maxSentenceCnt / 2 ) );
            tmpSentences = ( TSENTENCE * ) realloc ( tmpSentences, maxSentenceCnt * sizeof( *tmpSentences ) );
        }
                
        sentence.sentence = ( char * ) malloc ( tmpSentenceSize * sizeof( *tmpSentence ) );
        sentence.sentenceLower = ( char * ) malloc ( tmpSentenceSize * sizeof( *tmpSentence ) );
        
        strncpy( sentence.sentence, tmpSentence + stripChars, tmpSentenceSize );
        strncpy( sentence.sentenceLower, tmpSentence + stripChars, tmpSentenceSize );

        sentence.sentenceLength = strlen ( sentence.sentence );
        
        for ( int i = 0; i < sentence.sentenceLength; i++ )
            sentence.sentenceLower[i] = tolower( sentence.sentenceLower[i] );
        
        tmpSentences[sentenceCnt++] = sentence;
        
    }
    
    free ( tmpSentence );
        
    *sentences = tmpSentences;
    *sentenceCount = sentenceCnt;
    return 1;
    
}

int sentenceCmp ( TSENTENCE * s1, TSENTENCE * s2 ) {
    return ( s1->frequence < s2->frequence ) - ( s2->frequence < s1->frequence );
}

void searchPhrases ( TSENTENCE * sentences, int sentenceCount ) {
    
    char * searchedPhrase = NULL;
    size_t searchedSize;
    int foundCount = 0, found[50], result;
    
    while ( ( result = getline( &searchedPhrase, &searchedSize, stdin ) ) != EOF ) {
        
        int searchedPhraseLength = strlen ( searchedPhrase );
        
        //Strip newline
        if ( searchedPhraseLength > 1 && searchedPhrase[searchedPhraseLength - 1] == '\n' )
            searchedPhrase[ --searchedPhraseLength ] = '\0';
        
        //Lower the searched phrase
        for ( int i = 0; i < searchedPhraseLength; i++ )
            searchedPhrase[i] = tolower( searchedPhrase[i] );
        
        for ( int i = 0; i < sentenceCount; i++ )
            if ( matchesPhrase ( sentences[i].sentenceLower, sentences[i].sentenceLength,
                                 searchedPhrase, searchedPhraseLength ) )
                found[foundCount++] = i;
        
        printf("Nalezeno: %d\n", foundCount);
        
        for ( int i = 0; i < foundCount; i++ )
            printf( "> %s\n", sentences[ found[i] ].sentence );
        
        foundCount = 0;
        
    }
    
    free ( searchedPhrase );
    
}

int matchesPhrase ( char * sentence, int sentenceLength, char * phrase, int phraseLength ) {
        
    //Obvious - we cannot find it here
    if (phraseLength > sentenceLength)
        return 0;
    
    //Why should we search further
    for ( int i = 0; i < sentenceLength - phraseLength + 1; i++ )
        if ( strncmp ( sentence + i, phrase, phraseLength ) == 0 )
            return 1;
    
    return 0;
    
}
