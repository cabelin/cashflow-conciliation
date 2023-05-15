package br.challenge.smartcashflowapp.converters

import br.challenge.smartcashflowapp.dtos.TransactionDto
import br.challenge.smartcashflowapp.entities.TransactionEntity

open class TransactionConverter {
    open fun toDto(entity: TransactionEntity): TransactionDto {
        return TransactionDto(
            id = entity.id,
            relatedTransactionId = entity.relatedTransactionId,
            description = entity.description,
            installment = entity.installment,
            amount = entity.amount,
            currency = entity.currency,
            type = entity.type,
            category = entity.category,
            expectedPaymentDate = entity.expectedPaymentDate,
            paymentDate = entity.paymentDate,
            status = entity.status
        )
    }
}
