package br.challenge.smartcashflowapp.converters

import br.challenge.smartcashflowapp.dtos.constants.TransactionCategory
import br.challenge.smartcashflowapp.dtos.constants.TransactionCurrency
import br.challenge.smartcashflowapp.dtos.constants.TransactionStatus
import br.challenge.smartcashflowapp.dtos.constants.TransactionType
import br.challenge.smartcashflowapp.entities.TransactionEntity
import br.challenge.smartcashflowapp.utils.AssertionTestUtils
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
class TransactionConverterTest {

    @InjectMocks
    private lateinit var converter: TransactionConverter

    @ParameterizedTest
    @CsvSource("null", "2023-05-14")
    fun `should convert transaction to dto`(paymentDateS: String?) {
        // Given
        val paymentDate = if (paymentDateS == "null") null else LocalDate.parse(paymentDateS)

        val entity = TransactionEntity(
            relatedTransactionId = "a6151602-6cee-421a-bafd-1fa44f281dd9",
            description = "Teste",
            installment = 1,
            _amount = BigDecimal.valueOf(293.47),
            currency = TransactionCurrency.BRL,
            type = TransactionType.IN,
            category = TransactionCategory.OTHER,
            expectedPaymentDate = LocalDate.parse("2023-05-12"),
            paymentDate = paymentDate
        )

        // When
        val dto = converter.toDto(entity)

        // Then
        assertThat(dto).isNotNull

        val expectedStatus = if (paymentDate != null) TransactionStatus.PAID else TransactionStatus.PENDING

        assertAll("transaction fields should match",
            {assertEquals(null, dto.id)},
            {assertNotNull(UUID.fromString(dto.relatedTransactionId))},
            {assertEquals("Teste", dto.description)},
            {assertEquals(1, dto.installment)},
            {AssertionTestUtils.assertBigDecimal(293.47, dto.amount)},
            {assertEquals(TransactionCurrency.BRL, dto.currency)},
            {assertEquals(TransactionType.IN, dto.type)},
            {assertEquals(TransactionCategory.OTHER, dto.category)},
            {assertEquals(LocalDate.parse("2023-05-12"), dto.expectedPaymentDate)},
            {assertEquals(paymentDate, dto.paymentDate)},
            {assertEquals(expectedStatus, dto.status)}
        );
    }
}
