package parse

import Expression

sealed class ParseResult {
    data class Success (val expression: Expression, val rest: String) : ParseResult()
    data class Failure (val reason: String) : ParseResult()
}