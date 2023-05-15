package br.challenge.smartcashflowapp.dtos.constants

enum class TransactionCategory {
    SALES, UTILITIES, EMPLOYEE_SALARIES, INVENTORY_COSTS, MARKETING_EXPENSES, OTHER
}

enum class TransactionType {
    IN, OUT;
}

enum class TransactionStatus {
    PENDING, PAID
}

enum class TransactionCurrency {
    BRL
}

enum class TransactionInstallmentType {
    NONE, DAY, MONTH
}

enum class TransactionReportType {
    DAY, MONTH
}
