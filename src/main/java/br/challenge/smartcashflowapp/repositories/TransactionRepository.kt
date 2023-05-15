package br.challenge.smartcashflowapp.repositories

import br.challenge.smartcashflowapp.entities.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllByRelatedTransactionId(id: String): List<TransactionEntity>

    @Query(
        value = "SELECT * FROM TRANSACTION t WHERE t.EXPECTED_PAYMENT_DATE = :date OR t.PAYMENT_DATE = :date",
        nativeQuery = true
    )
    fun findAllByExpectedPaymentDateOrPaymentDate(@Param("date") date: LocalDate): List<TransactionEntity>
}
