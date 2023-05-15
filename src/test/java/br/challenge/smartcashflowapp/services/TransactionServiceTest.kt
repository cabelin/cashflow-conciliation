package br.challenge.smartcashflowapp.services

import br.challenge.smartcashflowapp.converters.TransactionConverter
import br.challenge.smartcashflowapp.dtos.TransactionCreateRequestDto
import br.challenge.smartcashflowapp.dtos.TransactionDto
import br.challenge.smartcashflowapp.dtos.TransactionReportResponseDto
import br.challenge.smartcashflowapp.dtos.constants.*
import br.challenge.smartcashflowapp.entities.TransactionEntity
import br.challenge.smartcashflowapp.repositories.TransactionRepository
import br.challenge.smartcashflowapp.utils.AssertionTestUtils.Companion.assertBigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
class TransactionServiceTest {

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var transactionConverter: TransactionConverter

    @Test
    fun `should create transaction with one installment`() {
        // Given
        val createRequestDto = TransactionCreateRequestDto(
            description = "Teste",
            installmentsCount = 1,
            amount = BigDecimal.valueOf(100),
            type = TransactionType.IN,
            category = TransactionCategory.SALES,
            expectedPaymentDate = LocalDate.parse("2023-05-12"),
            paymentDate = null
        )

        val transactionToSaveArgumentCaptor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<TransactionEntity>>

        // When
        transactionService.createTransaction(createRequestDto)

        // Then
        Mockito.verify(transactionRepository).saveAll(transactionToSaveArgumentCaptor.capture())

        val entities = transactionToSaveArgumentCaptor.value

        val actual = entities[0]
        assertAll("transaction fields should match",
            {assertEquals(null, actual.id)},
            { Assertions.assertNotNull(UUID.fromString(actual.relatedTransactionId))},
            {assertEquals("Teste", actual.description)},
            {assertEquals(1, actual.installment)},
            {assertEquals(BigDecimal.valueOf(100).compareTo(actual.amount), 0)},
            {assertEquals(TransactionCurrency.BRL, actual.currency)},
            {assertEquals(TransactionType.IN, actual.type)},
            {assertEquals(TransactionCategory.SALES, actual.category)},
            {assertEquals(LocalDate.parse("2023-05-12"), actual.expectedPaymentDate)},
            {assertEquals(null, actual.paymentDate)},
            {assertEquals(TransactionStatus.PENDING, actual.status)}
        )
    }

    @Test
    fun `should create transaction with more than one installment`() {
        // Given
        val installmentCount = 5

        val createRequestDto = TransactionCreateRequestDto(
            description = "Teste",
            installmentsCount = installmentCount,
            amount = BigDecimal.valueOf(101.48),
            type = TransactionType.IN,
            category = TransactionCategory.SALES,
            expectedPaymentDate = LocalDate.parse("2023-05-12"),
            paymentDate = LocalDate.parse("2023-05-12"),
        )

        val transactionsToSaveArgumentCaptor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<TransactionEntity>>

        // When
        transactionService.createTransaction(createRequestDto)

        // Then
        Mockito.verify(transactionRepository).saveAll(transactionsToSaveArgumentCaptor.capture())

        val entities = transactionsToSaveArgumentCaptor.value

        val entityFirst = entities[0]
        assertNotNull(UUID.fromString(entityFirst.relatedTransactionId))
        assertThat(entities)
            .allMatch { it.relatedTransactionId == entityFirst.relatedTransactionId }
            .allMatch { it.description == entityFirst.description }
            .allMatch { it.type == entityFirst.type }
            .allMatch { it.category == entityFirst.category }
            .allMatch { it.currency == entityFirst.currency }

        assertAll("transaction fields from first installment should match",
            {assertEquals(null, entityFirst.id)},
            {assertEquals(1, entityFirst.installment)},
            {assertBigDecimal(20.28, entityFirst.amount)},
            {assertEquals(LocalDate.parse("2023-05-12"), entityFirst.expectedPaymentDate)},
            {assertEquals(LocalDate.parse("2023-05-12"), entityFirst.paymentDate)},
            {assertEquals(TransactionStatus.PAID, entityFirst.status)}
        )

        assertAll("transaction fields from others installments should match",
            {repeat(installmentCount - 1) {
                val installmentIndex = it + 1
                val installment = installmentIndex + 1
                val entityOther = entities[installmentIndex]

                assertEquals(null, entityOther.id)
                assertEquals(installment, entityOther.installment)
                assertBigDecimal(20.30, entityOther.amount)
                assertEquals(LocalDate.parse("2023-05-12"), entityOther.expectedPaymentDate)
                assertEquals(null, entityOther.paymentDate)
                assertEquals(TransactionStatus.PENDING, entityOther.status)
            }}
        )
    }

    @Test
    fun `unknown report type throws exception`() {
        val date = LocalDate.of(2022, 5, 15)
        val unknownType = TransactionReportType.MONTH

        assertThrows(IllegalArgumentException::class.java) {
            transactionService.report(unknownType, date)
        }
    }

    @Test
    fun `should dayReport returns expected result`() {
        // Given
        val date = LocalDate.of(2023, 5, 15)

        val dtos = listOf(
            TransactionDto(
                id = null,
                relatedTransactionId = null,
                amount = BigDecimal.valueOf(101.0),
                type = TransactionType.IN,
                installment = 1,
                description = "Teste day report 1",
                expectedPaymentDate = LocalDate.parse("2023-05-15"),
                paymentDate = LocalDate.parse("2023-05-15"),
                category = TransactionCategory.OTHER,
                currency = TransactionCurrency.BRL,
                status = TransactionStatus.PAID
            ),
            TransactionDto(
                id = null,
                relatedTransactionId = null,
                amount = BigDecimal.valueOf(-102.0),
                type = TransactionType.OUT,
                installment = 1,
                description = "Teste day report 2",
                expectedPaymentDate = LocalDate.parse("2023-05-13"),
                paymentDate = LocalDate.parse("2023-05-15"),
                category = TransactionCategory.OTHER,
                currency = TransactionCurrency.BRL,
                status = TransactionStatus.PAID
            ),
            TransactionDto(
                id = null,
                relatedTransactionId = null,
                amount = BigDecimal.valueOf(-99.2),
                type = TransactionType.OUT,
                installment = 1,
                description = "Teste day report 3",
                expectedPaymentDate = LocalDate.parse("2023-05-15"),
                paymentDate = null,
                category = TransactionCategory.OTHER,
                currency = TransactionCurrency.BRL,
                status = TransactionStatus.PENDING
            )
        )

        val expected = TransactionReportResponseDto(
            date = date,
            amount = BigDecimal.valueOf(-100.2),
            paidAmount = BigDecimal.valueOf(-1.0),
            pendingAmount = BigDecimal.valueOf(-99.2),
            transactions = dtos
        )

        val entities = listOf(
            TransactionEntity().copy(
                _amount = BigDecimal.valueOf(101.0),
                type = TransactionType.IN,
                installment = 1,
                description = "Teste day report 1",
                expectedPaymentDate = LocalDate.parse("2023-05-15"),
                paymentDate = LocalDate.parse("2023-05-15")
            ),
            TransactionEntity().copy(
                _amount = BigDecimal.valueOf(102.0),
                type = TransactionType.OUT,
                installment = 1,
                description = "Teste day report 2",
                expectedPaymentDate = LocalDate.parse("2023-05-13"),
                paymentDate = LocalDate.parse("2023-05-15")
            ),
            TransactionEntity().copy(
                _amount = BigDecimal.valueOf(99.2),
                type = TransactionType.OUT,
                installment = 1,
                description = "Teste day report 3",
                expectedPaymentDate = LocalDate.parse("2023-05-15"),
                paymentDate = null
            )
        );

        Mockito.`when`(transactionRepository.findAllByExpectedPaymentDateOrPaymentDate(date))
            .thenReturn(entities)

        entities.forEachIndexed { index, entity ->
            Mockito.`when`(transactionConverter.toDto(entity))
                .thenReturn(dtos[index])
        }

        // When
        val result = transactionService.report(TransactionReportType.DAY, date)

        // Then
        assertEquals(expected, result)
    }
}
