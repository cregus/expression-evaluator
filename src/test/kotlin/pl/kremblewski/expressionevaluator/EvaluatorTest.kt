package pl.kremblewski.expressionevaluator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Suppress("unused")
class EvaluatorTest {
    @Test
    fun `should evaluate number`() {
        assertEquals(1.toBigDecimal(), evaluate("1"))
        assertEquals(10.toBigDecimal(), evaluate("10"))
        assertEquals(1.5.toBigDecimal(), evaluate("1.5"))
    }

    @Test
    fun `should evaluate unary`() {
        assertEquals(1.toBigDecimal(), evaluate("+1"))
        assertEquals((-1).toBigDecimal(), evaluate("-1"))
    }

    @Test
    fun `should evaluate default operators`() {
        assertEquals(110.toBigDecimal(), evaluate("100+10"))
        assertEquals(90.toBigDecimal(), evaluate("100-10"))
        assertEquals(1000.toBigDecimal(), evaluate("100*10"))
        assertEquals(10.toBigDecimal(), evaluate("100/10"))
        assertEquals(100.toBigDecimal(), evaluate("10^2"))
        assertEquals(1.toBigDecimal(), evaluate("10%3"))
        assertEquals(8.toBigDecimal(), evaluate("2+3*2"))
    }

    @Test
    fun `should evaluate variable`() {
        val data = mapOf("x" to 1, "y" to 2)
        assertEquals(1.toBigDecimal(), evaluate("x", data))
        assertEquals((-1).toBigDecimal(), evaluate("-x", data))
        assertEquals(3.toBigDecimal(), evaluate("x+y", data))
        assertEquals(8.toBigDecimal(), evaluate("max(x,y)^3", data))
    }

    @Test
    fun `should evaluate parentheses`() {
        assertEquals(10.toBigDecimal(), evaluate("2*(3+2)"))
        assertEquals(5.toBigDecimal(), evaluate("10+-(3+2)"))
    }

    @Test
    fun `should evaluate function`() {
        assertEquals(1.toBigDecimal(), evaluate("abs(-1)"))
        assertEquals(4.toBigDecimal(), evaluate("sqrt(16)"))
        assertEquals(3.toBigDecimal(), evaluate("cbrt(27)"))
        assertEquals(2.toBigDecimal(), evaluate("ceil(1.01)"))
        assertEquals(1.toBigDecimal(), evaluate("floor(1.99)"))
        assertEquals(0.5.toBigDecimal(), evaluate("sin(30)"))
        assertEquals(0.5.toBigDecimal(), evaluate("cos(60)"))

        assertEquals(3.33.toBigDecimal(), evaluate("round(10/3,2)"))
        assertEquals(1.toBigDecimal(), evaluate("min(1,2)"))
        assertEquals(2.toBigDecimal(), evaluate("max(1,2)"))

        assertEquals(3.toBigDecimal(), evaluate("min(1,2)+max(1,2)"))
        assertEquals((-2.8).toBigDecimal(), evaluate("round(-sqrt(min(8,9)),1)"))
    }
}