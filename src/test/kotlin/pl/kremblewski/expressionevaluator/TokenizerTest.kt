package pl.kremblewski.expressionevaluator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Suppress("unused")
class TokenizerTest {
    @Test
    fun `should tokenize number`() {
        assertEquals(Token.Number(1), tokenize("1"))
        assertEquals(Token.Number(10), tokenize("10"))
        assertEquals(Token.Number(1.5), tokenize("1.5"))
    }

    @Test
    fun `should tokenize unary`() {
        assertEquals(Token.Unary.Plus(Token.Number(1)), tokenize("+1"))
        assertEquals(Token.Unary.Minus(Token.Number(1)), tokenize("-1"))
    }

    @Test
    fun `should tokenize operator`() {
        assertEquals(
            Token.Operator(
                leftToken = Token.Number(100),
                rightToken = Token.Number(15),
                operation = DefaultOperators.addition
            ),
            tokenize("100+15")
        )
        assertEquals(
            Token.Operator(
                leftToken = Token.Number(1),
                rightToken = Token.Operator(
                    leftToken = Token.Number(2),
                    rightToken = Token.Number(4),
                    operation = DefaultOperators.addition
                ),
                operation = DefaultOperators.addition
            ),
            tokenize("1+2+4")
        )
    }

    @Test
    fun `should tokenize variable`() {
        val data = mapOf("x" to 1, "y" to 2)
        val xVariable = Token.Variable("x", 1)
        val yVariable = Token.Variable("y", 2)

        assertEquals(xVariable, tokenize("x", data))
        assertEquals(
            Token.Unary.Minus(xVariable),
            tokenize("-x", data)
        )
        assertEquals(
            Token.Operator(
                leftToken = xVariable,
                rightToken = yVariable,
                operation = DefaultOperators.addition
            ),
            tokenize("x+y", data)
        )
    }

    @Test
    fun `should tokenize parentheses`() {
        assertEquals(
            Token.Operator(
                leftToken = Token.Number(2),
                rightToken = Token.Function.OneParam(
                    name = "",
                    token = Token.Operator(
                        leftToken = Token.Number(3),
                        rightToken = Token.Number(2),
                        operation = DefaultOperators.addition
                    ),
                    function1 = DefaultFunctions.parentheses
                ),
                operation = DefaultOperators.multiplication
            ),
            tokenize("2*(3+2)")
        )
        assertEquals(
            Token.Operator(
                leftToken = Token.Number(10),
                rightToken = Token.Unary.Minus(
                    Token.Function.OneParam(
                        name = "",
                        token = Token.Operator(
                            leftToken = Token.Number(3),
                            rightToken = Token.Number(2),
                            operation = DefaultOperators.addition
                        ),
                        function1 = DefaultFunctions.parentheses
                    )
                ),
                operation = DefaultOperators.addition
            ),
            tokenize("10+-(3+2)")
        )
    }

    @Test
    fun `should tokenize function`() {
        assertEquals(
            Token.Function.OneParam(
                name = "sqrt",
                token = Token.Number(16),
                function1 = DefaultFunctions.squareRoot
            ),
            tokenize("sqrt(16)")
        )
        assertEquals(
            Token.Function.TwoParams(
                name = "round",
                firstToken = Token.Operator(
                    leftToken = Token.Number(10),
                    rightToken = Token.Number(3),
                    operation = DefaultOperators.division
                ),
                secondToken = Token.Number(2),
                function2 = DefaultFunctions.round
            ),
            tokenize("round(10/3,2)")
        )
        assertEquals(
            Token.Operator(
                leftToken = Token.Function.TwoParams(
                    name = "min",
                    firstToken = Token.Number(1),
                    secondToken = Token.Number(2),
                    function2 = DefaultFunctions.min
                ),
                rightToken = Token.Function.TwoParams(
                    name = "max",
                    firstToken = Token.Number(1),
                    secondToken = Token.Number(2),
                    function2 = DefaultFunctions.max
                ),
                operation = DefaultOperators.addition
            ),
            tokenize("min(1,2)+max(1,2)")
        )
        assertEquals(
            Token.Function.TwoParams(
                name = "round",
                firstToken = Token.Unary.Minus(
                    token = Token.Function.OneParam(
                        name = "sqrt",
                        token = Token.Function.TwoParams(
                            name = "min",
                            firstToken = Token.Number(8),
                            secondToken = Token.Number(9),
                            function2 = DefaultFunctions.min
                        ),
                        function1 = DefaultFunctions.squareRoot
                    )
                ),
                secondToken = Token.Number(1),
                function2 = DefaultFunctions.round
            ),
            tokenize("round(-sqrt(min(8,9)),1)")
        )
    }
}