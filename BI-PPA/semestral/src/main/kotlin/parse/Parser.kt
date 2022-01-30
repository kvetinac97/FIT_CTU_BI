package parse

import AND
import Expression.*
import Expression
import FALSE
import MINUS
import MLT
import OR
import PLUS
import TRUE
import ap
import eval.reducePrint
import minify
import num

import parse.ParseResult.*

fun printParse (str: String, applicative: Boolean = false, newline: Boolean = true) = parseExpression(str).let { result ->
    when (result) {
        is Failure -> println("Error: ${result.reason}")
        is Success -> if (result.rest.trim().isEmpty())
            reducePrint(result.expression, applicative)
                .apply { println("Result: ${minify(this, true)}" + if (newline) "\n" else "") }
            else println("Error: string was not fully consumed, ${result.rest} remaining")
    }
}

tailrec fun parseExpression (from: String) : ParseResult =
    if (from.isEmpty()) Failure("Expected variable or (, got empty string")
    else when(from.first()) {
        in 'a'..'z' -> Success(Variable(from.first().toString()), from.drop(1))
        in '0'..'9' -> Success(num(from.first() - '0'), from.drop(1))
        'T' -> Success(TRUE, from.drop(1))
        'F' -> Success(FALSE, from.drop(1))
        ' ' -> parseExpression(from.drop(1))
        '(' -> parseExpressionInner(from.drop(1)).let { result ->
            when(result) {
                is Failure -> result
                is Success -> parseEndBracket(result.rest, result.expression)
            }
        }
        else -> Failure("Expected variable or (, got $from")
    }

tailrec fun parseExpressionInner (from: String) : ParseResult =
    if (from.isEmpty()) Failure("Expected variable, λ or (, got empty string")
    else when(from.first()) {
        ' ' -> parseExpressionInner(from.drop(1))
        '+' -> parseDoubleFun(from.drop(1), PLUS)
        '-' -> parseDoubleFun(from.drop(1), MINUS)
        '*' -> parseDoubleFun(from.drop(1), MLT)
        '&' -> parseDoubleFun(from.drop(1), AND)
        '|' -> parseDoubleFun(from.drop(1), OR)
        'λ' -> parseLambda1(from)
        else -> parseExpression(from).let { result ->
            when(result) {
                is Failure -> result
                is Success -> parseApplication1(result.rest).let { result2 ->
                    when (result2) {
                        is Failure -> result2
                        is Success -> Success(Application(result.expression, result2.expression), result2.rest)
                    }
                }
            }
        }
    }

fun parseDoubleFun (from: String, expression: Expression) : ParseResult =
    parseExpression(from).let { result ->
        when (result) {
            is Failure -> result
            is Success -> parseExpression(result.rest).let { result2 ->
                when(result2) {
                    is Failure -> result2
                    is Success -> Success(expression ap result.expression ap result2.expression, result2.rest)
                }
            }
        }
    }

fun parseApplication1 (from: String) : ParseResult =
    if (from.isEmpty()) Failure("Expected space, got empty string")
    else when(from.first()) {
        ' ' -> parseExpression(from.drop(1))
        else -> Failure("Expected space, got $from")
    }

tailrec fun parseLambda1 (from: String) : ParseResult =
    if (from.isEmpty()) Failure("Expected λ, got empty string")
    else when (from.first()) {
        ' ' -> parseLambda1(from.drop(1))
        'λ' -> parseArg(from.drop(1))
        else -> Failure("Expected λ, got $from")
    }

tailrec fun parseArg (from: String, args: Set<String> = setOf()) : ParseResult =
    if (from.isEmpty()) Failure("Expected argument, got empty string")
    else when(from.first()) {
        ' ' -> parseArg(from.drop(1), args)
        '.' -> parseDot(from, args)
        in 'a'..'z' -> parseArg(from.drop(1), args + from.first().toString())
        else -> Failure("Expected argument, got $from")
    }

tailrec fun parseDot (from: String, args: Set<String>) : ParseResult =
    if (from.isEmpty()) Failure("Expected ., got empty string")
    else when(from.first()) {
        ' ' -> parseDot(from.drop(1), args)
        '.' -> parseExpression(from.drop(1)).let { result ->
            when(result) {
                is Failure -> result
                is Success -> Success(LFunction(args, result.expression), result.rest)
            }
        }
        else -> Failure("Expected ., got $from")
    }

tailrec fun parseEndBracket (from: String, expression: Expression) : ParseResult =
    if (from.isEmpty()) Failure("Expected ), got empty string")
    else when (from.first()) {
        ' ' -> parseEndBracket(from.drop(1), expression)
        ')' -> Success(expression, from.drop(1))
        else -> Failure("Expected ), got $from")
    }
