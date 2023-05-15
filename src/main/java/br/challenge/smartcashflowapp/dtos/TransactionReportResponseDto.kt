package br.challenge.smartcashflowapp.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class TransactionReportResponseDto (
    val date: LocalDate,

    val amount: BigDecimal,

    val paidAmount: BigDecimal,

    val pendingAmount: BigDecimal,

    val transactions: List<TransactionDto>
)
