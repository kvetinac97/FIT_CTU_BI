# Semestral work PPA

Lambda expression evaluator in Kotlin (functional)

## Usage

I recommend viewing the tests at [BasicTest.kt](src/test/kotlin/BasicTest.kt) and [ParseTest.kt](src/test/kotlin/ParseTest.kt), where you can see using DSL for expression creation in code and also the format required for parsing input from the command line / string.

The best way to run the project is with IntelliJ IDEA, in case if you need it, you can also compile it with `kotlinc` into jar file

`kotlinc src/main/kotlin/*.kt src/main/kotlin/eval/*.kt src/main/kotlin/parse/*.kt -include-runtime -d basic.jar -d wrzecond.jar`

and then run it with `java -jar wrzecond.jar`.

## Implementation

Lambda expressions are implemented as algebraic data type  (Expression := Variable OR LFunction OR Application), which enables using of pattern matching (in Kotlin `when(expression) { is Expression -> ... }` ).

Each lambda expression keeps track of its unbound variables and has a function for printing in human readable format. Printing is even optimized, so an expression like `((a b) c)` will be printed as `(a b c)`.

For easier work with lambda expressions, I created DSL _(domain specific language)_, which enables composing expressions with functions  **v(a)** _(creates a variable with name a)_, **lambda, nlambda** _(creates a lambda function, lambda function with more variables or named lambda function)_ and inline function **ap** _(creates an application)_. Thanks to operation priority in Kotlin, you can call `a ap b ap c`, which is correctly interpreted as `((a b) c)`.

Frequently used "constants" - lambda functions such as Church numerals, arithmetic operations, Y combinator or factorial are globally defined in file `Constants.kt`.

When evaluating a lambda expression, steps are executed with following priority: Expanding lambda function with more variables / named lambda function => Eta conversion => Alfa reduction _(if it is needed due to Beta reduction)_ => Beta reduction. In case of applicative evaluation, the substituted expression is evaluated first.

Expression parsing is performed thanks to functions working with parsed string and another algebraic data type `ParseResult` representing state of parsing, which is either `Failure` (contains an error message) or  `Success` (contains parsed lambda expression and rest of the parsed string).

The program is capable of parsing only the most basic lambda expressions. Brackets and spaces are required everywhere - `(λa.aa)` won't be parsed as the application has to be written as `(λa. (a a))`. The only improvement is parsing lambda functions of more variables - `(λab.(a b))` is a valid expression which will be parsed.

Parser is also capable of some basic macros - numbers from 0 to 9, arithmetic and logic operations (+, -, *, &, |) and T, F representing True / False.

## Tasks:

 - [x] representing expression (Expression)
 - [x] known expressions, constants, Church numerals (Constants)
 - [x] beta reduction (BetaReduction) and normal evaluation (Evaluator)
 - [x] alfa reduction (AlfaReduction) - if needed
 - [x] eta conversion (EtaReduction)
 - [x] named lambda expression and its expansion
 - [x] fully-stepped evaluation (step = alfa reduction, beta reduction, eta conversion, expansion)
 - [x] lambda functions with more variables
 - [x] applicative evaluation
 - [x] loading user input, invalid input detection (Parser)

&copy; Ondřej Wrzecionko 2021
