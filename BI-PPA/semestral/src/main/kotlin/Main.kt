import parse.printParse

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("=== LAMBDA CALCULUS EVALUATOR ===")

            // Mode selection
            println("1 = normal mode (default)")
            println("2 = applicative mode")
            val mode = readLine()?.toIntOrNull() ?: 1

            // Evaluating
            println("Type lambda expressions, EOF = end")
            println("1) variable = lowercase letter")
            println("2) lambda function = (λa.a), (λab.a) ... - multiple args supported")
            println("3) application = (a b), ((a b) c), (s (s z)) ...")
            println("The brackets in lambda functions and applications are MANDATORY")
            println("Supported shortcuts: numbers 0-9, +, -, *, T (true), F (false), & (and), | (or)")
            loadLambda(mode)
        }

        private const val NORMAL_MODE = 1

        private fun loadLambda(mode: Int) : Unit? = readLine()?.let { line ->
            printParse(line, mode == NORMAL_MODE, false)
            loadLambda(mode)
        }
    }
}