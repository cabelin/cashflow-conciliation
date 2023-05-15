package br.challenge.smartcashflowapp.utils

import org.junit.jupiter.api.Assertions
import java.math.BigDecimal

class AssertionTestUtils {
    companion object {
        fun assertBigDecimal(expected: Double, actual: Double) {
            val expectedBigDecimal = BigDecimal.valueOf(expected);
            val actualBigDecimal = BigDecimal.valueOf(actual);
            assertBigDecimal(expectedBigDecimal, actualBigDecimal)
        }

        fun assertBigDecimal(expected: Double, actual: BigDecimal) {
            val expectedBigDecimal = BigDecimal.valueOf(expected);
            assertBigDecimal(expectedBigDecimal, actual)
        }

        fun assertBigDecimal(expected: BigDecimal, actual: BigDecimal) {
            Assertions.assertNotNull(expected)
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expected.compareTo(actual), 0, "expected: <$expected> but was: <$actual>")
        }
    }

}