package br.challenge.smartcashflowapp.dtos

import br.challenge.smartcashflowapp.dtos.constants.TransactionCategory
import br.challenge.smartcashflowapp.dtos.constants.TransactionInstallmentType
import br.challenge.smartcashflowapp.dtos.constants.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TransactionCreateRequestDto @JvmOverloads constructor(
    @field:NotEmpty(message = "this is a required field")
    val description: String = "",

    @field:NotNull(message = "this is a required field")
    val installmentsCount: Int? = 1,

    val installmentsType: TransactionInstallmentType = TransactionInstallmentType.NONE,

    @field:NotNull(message = "this is a required field")
    val amount: BigDecimal? = null,

    @field:NotNull(message = "this is a required field, the options are: IN, OUT")
    val type: TransactionType? = null,

    @field:NotNull(message = "this is a required field, the options are: SALES, UTILITIES, EMPLOYEE_SALARIES, INVENTORY_COSTS, MARKETING_EXPENSES, OTHER")
    val category: TransactionCategory? = null,

    @field:NotNull(message = "this is a required field")
    val expectedPaymentDate: LocalDate? = null,

    val paymentDate: LocalDate?
)
