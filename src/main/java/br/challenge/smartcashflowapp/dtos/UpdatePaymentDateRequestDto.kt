package br.challenge.smartcashflowapp.dtos

import java.time.LocalDate
import javax.validation.constraints.NotNull

data class UpdatePaymentDateRequestDto (
    @field:NotNull(message = "this is a required field")
    val paymentDate: LocalDate
)
