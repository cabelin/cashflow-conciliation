package br.challenge.smartcashflowapp.repositories

import br.challenge.smartcashflowapp.dtos.constants.TransactionCategory
import br.challenge.smartcashflowapp.dtos.constants.TransactionCurrency
import br.challenge.smartcashflowapp.dtos.constants.TransactionType
import br.challenge.smartcashflowapp.entities.TransactionEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.math.BigDecimal
import java.time.LocalDate

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private lateinit var repository: TransactionRepository

    @Test
    fun `test save transaction`() {
        // Given
        val transaction = TransactionEntity(
            description = "Test transaction",
            relatedTransactionId = "a6151602-6cee-421a-bafd-1fa44f281dd9",
            _amount = BigDecimal.TEN,
            currency = TransactionCurrency.BRL,
            type = TransactionType.IN,
            category = TransactionCategory.SALES,
            expectedPaymentDate = LocalDate.parse("2023-05-12"),
            paymentDate = LocalDate.parse("2023-05-12")
        )

        // When
        val savedTransaction = repository.save(transaction)

        // Then
        assertNotNull(savedTransaction.id)
        assertEquals("Test transaction", savedTransaction.description)
        assertEquals("a6151602-6cee-421a-bafd-1fa44f281dd9", savedTransaction.relatedTransactionId)
        assertEquals(BigDecimal.TEN, savedTransaction.amount)
        assertEquals(TransactionCurrency.BRL, savedTransaction.currency)
        assertEquals(TransactionType.IN, savedTransaction.type)
        assertEquals(TransactionCategory.SALES, savedTransaction.category)
        assertEquals(LocalDate.parse("2023-05-12"), savedTransaction.expectedPaymentDate)
        assertEquals(LocalDate.parse("2023-05-12"), savedTransaction.paymentDate)
    }

    @Test
    fun `test find transaction by id`() {
        // Given
        val transaction = TransactionEntity(
            description = "Test transaction",
            relatedTransactionId = "a6151602-6cee-421a-bafd-1fa44f281dd9",
            _amount = BigDecimal.TEN,
            currency = TransactionCurrency.BRL,
            type = TransactionType.IN,
            category = TransactionCategory.SALES,
            expectedPaymentDate = LocalDate.now(),
            paymentDate = LocalDate.now(),
        )
        val savedTransaction = repository.save(transaction)

        // When
        val foundTransaction = repository.findById(savedTransaction.id!!)

        // Then
        assertTrue(foundTransaction.isPresent)
        assertEquals(savedTransaction.description, foundTransaction.get().description)
        assertEquals("a6151602-6cee-421a-bafd-1fa44f281dd9", foundTransaction.get().relatedTransactionId)
        assertEquals(savedTransaction.amount, foundTransaction.get().amount)
        assertEquals(savedTransaction.currency, foundTransaction.get().currency)
        assertEquals(savedTransaction.type, foundTransaction.get().type)
        assertEquals(savedTransaction.category, foundTransaction.get().category)
        assertEquals(savedTransaction.expectedPaymentDate, foundTransaction.get().expectedPaymentDate)
        assertEquals(savedTransaction.paymentDate, foundTransaction.get().paymentDate)
    }

    @Test
    fun `test delete transaction by id`() {
        // Given
        val transaction = TransactionEntity(
            description = "Test transaction",
            relatedTransactionId = "a6151602-6cee-421a-bafd-1fa44f281dd9",
            _amount = BigDecimal.TEN,
            currency = TransactionCurrency.BRL,
            type = TransactionType.IN,
            category = TransactionCategory.SALES,
            expectedPaymentDate = LocalDate.now(),
        )
        val savedTransaction = repository.save(transaction)

        // When
        repository.deleteById(savedTransaction.id!!)

        // Then
        assertFalse(repository.findById(savedTransaction.id!!).isPresent)
    }
}