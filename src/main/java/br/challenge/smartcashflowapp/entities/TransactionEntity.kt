package br.challenge.smartcashflowapp.entities

import br.challenge.smartcashflowapp.dtos.constants.TransactionCategory
import br.challenge.smartcashflowapp.dtos.constants.TransactionCurrency
import br.challenge.smartcashflowapp.dtos.constants.TransactionStatus
import br.challenge.smartcashflowapp.dtos.constants.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "TRANSACTION")
data class TransactionEntity(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "RELATED_TRANSACTION_ID")
    val relatedTransactionId: String?,

    @Column(name = "DESCRIPTION")
    val description: String,

    @Column(name = "INSTALLMENT")
    val installment: Int = 1,

    @Column(name = "AMOUNT")
    val _amount: BigDecimal,

    @Column(name = "CURRENCY")
    @Enumerated(value = EnumType.STRING)
    val currency: TransactionCurrency,

    @Column(name = "TYPE")
    @Enumerated(value = EnumType.STRING)
    val type: TransactionType,

    @Column(name = "CATEGORY")
    @Enumerated(value = EnumType.STRING)
    val category: TransactionCategory,

    @Column(name = "EXPECTED_PAYMENT_DATE")
    val expectedPaymentDate: LocalDate,

    @Column(name = "PAYMENT_DATE")
    val paymentDate: LocalDate? = null,

    ) : BaseEntity() {

    constructor() : this(
        null,
        null,
        "",
        1,
        BigDecimal.ZERO,
        TransactionCurrency.BRL,
        TransactionType.IN,
        TransactionCategory.OTHER,
        LocalDate.now(),
        null
    )

    val status: TransactionStatus
        get() {
            if (paymentDate != null) {
                return TransactionStatus.PAID
            }
            return TransactionStatus.PENDING
        }

    val amount: BigDecimal
        get() {
            if(type == TransactionType.OUT) {
                return BigDecimal.valueOf(-1).multiply(_amount)
            }
            return _amount
        }
}
