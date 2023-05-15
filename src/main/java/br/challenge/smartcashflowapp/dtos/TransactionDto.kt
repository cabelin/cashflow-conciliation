package br.challenge.smartcashflowapp.dtos

import br.challenge.smartcashflowapp.dtos.constants.TransactionCategory
import br.challenge.smartcashflowapp.dtos.constants.TransactionCurrency
import br.challenge.smartcashflowapp.dtos.constants.TransactionStatus
import br.challenge.smartcashflowapp.dtos.constants.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

data class TransactionDto(
    val id: Long?,
    val relatedTransactionId: String?,
    val description: String,
    val installment: Int = 1,
    val amount: BigDecimal,
    val currency: TransactionCurrency,
    val type: TransactionType,
    val category: TransactionCategory,
    val expectedPaymentDate: LocalDate,
    val paymentDate: LocalDate?,
    val status: TransactionStatus
)
