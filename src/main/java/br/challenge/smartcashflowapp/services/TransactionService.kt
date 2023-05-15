package br.challenge.smartcashflowapp.services

import br.challenge.smartcashflowapp.converters.TransactionConverter
import br.challenge.smartcashflowapp.dtos.TransactionCreateRequestDto
import br.challenge.smartcashflowapp.dtos.TransactionDto
import br.challenge.smartcashflowapp.dtos.TransactionReportResponseDto
import br.challenge.smartcashflowapp.dtos.constants.TransactionCurrency
import br.challenge.smartcashflowapp.dtos.constants.TransactionInstallmentType
import br.challenge.smartcashflowapp.dtos.constants.TransactionReportType
import br.challenge.smartcashflowapp.dtos.constants.TransactionStatus
import br.challenge.smartcashflowapp.entities.TransactionEntity
import br.challenge.smartcashflowapp.repositories.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

@Service
class TransactionService(private val transactionRepository: TransactionRepository, private val transactionConverter: TransactionConverter) {
    fun createTransaction(dto: TransactionCreateRequestDto): List<TransactionDto> {
        val entities = createTransactionInstallments(dto)
        return transactionRepository.saveAll(entities).map { transactionConverter.toDto(it) }
    }

    fun findAllTransactions(pageable: Pageable): Page<TransactionDto> {
        return transactionRepository.findAll(pageable).map { e -> transactionConverter.toDto(e) }
    }

    fun report(type: TransactionReportType?, date: LocalDate): TransactionReportResponseDto {
        if (TransactionReportType.DAY == type) {
            return dayReport(date)
        }
        throw IllegalArgumentException("Does not exists report for this type $type")
    }

    private fun createTransactionInstallments(dto: TransactionCreateRequestDto): List<TransactionEntity> {
        val id = UUID.randomUUID().toString()
        val installmentAmounts = createInstallmentAmounts(dto.amount!!, BigDecimal(dto.installmentsCount!!))

        return installmentAmounts.mapIndexed { index, installmentAmount ->
            val installment = index + 1
            createTransactionInstallment(id, dto, installment, installmentAmount)
        }
    }

    private fun createInstallmentAmounts(amount: BigDecimal, installmentsCount: BigDecimal): List<BigDecimal> {
        val installmentAmountBase = (amount / installmentsCount).setScale(2, RoundingMode.HALF_UP)
        val installmentAmountFirst = amount - (installmentAmountBase * (installmentsCount - BigDecimal.ONE))
        return listOf(installmentAmountFirst) + List(installmentsCount.intValueExact() - 1) { installmentAmountBase }
    }

    private fun createTransactionInstallment(id: String?, dto: TransactionCreateRequestDto, installment: Int, installmentAmount: BigDecimal): TransactionEntity {
        return TransactionEntity(
            relatedTransactionId = id,
            description = dto.description,
            installment = installment,
            _amount = installmentAmount,
            currency = TransactionCurrency.BRL,
            type = dto.type!!,
            category = dto.category!!,
            expectedPaymentDate = calculateExpectedPaymentDate(installment, dto),
            paymentDate = calculatePaymentDate(installment, dto)
        )
    }

    private fun calculateExpectedPaymentDate(installment: Int, dto: TransactionCreateRequestDto): LocalDate {
        val plusInstallment = (installment - 1).toLong()
        return when (dto.installmentsType) {
            TransactionInstallmentType.MONTH -> dto.expectedPaymentDate!!.plusMonths(plusInstallment)
            TransactionInstallmentType.DAY -> dto.expectedPaymentDate!!.plusDays(plusInstallment)
            else -> dto.expectedPaymentDate!!
        }
    }

    private fun calculatePaymentDate(
        installment: Int,
        dto: TransactionCreateRequestDto
    ): LocalDate? {
        val isFirstInstallment = installment == 1
        return if (isFirstInstallment) dto.paymentDate else null
    }

    private fun dayReport(date: LocalDate): TransactionReportResponseDto {
        val transactions = transactionRepository.findAllByExpectedPaymentDateOrPaymentDate(date)
            .filterNot { isPaidInAnotherDay(it, date) }

        return TransactionReportResponseDto(
            date = date,
            amount = calculateAmount(transactions),
            pendingAmount = calculatePendingAmount(transactions),
            paidAmount = calculatePaidAmount(transactions),
            transactions = transactions.map { transactionConverter.toDto(it) }
        )
    }

    private fun isPaidInAnotherDay(
        it: TransactionEntity,
        date: LocalDate
    ) = it.status == TransactionStatus.PAID && it.paymentDate != date

    private fun calculateAmount(transactions: List<TransactionEntity>) =
        transactions.sumOf { it.amount }

    private fun calculatePendingAmount(transactions: List<TransactionEntity>) =
        transactions.filter { it.paymentDate == null }.sumOf { it.amount }

    private fun calculatePaidAmount(transactions: List<TransactionEntity>) =
        transactions.filter { it.paymentDate != null } .sumOf { it.amount }
}
