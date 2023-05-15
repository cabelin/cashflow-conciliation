package br.challenge.smartcashflowapp.configs

import br.challenge.smartcashflowapp.converters.TransactionConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConvertersConfig {
    @Bean
    open fun transactionConverter(): TransactionConverter {
        return TransactionConverter()
    }
}
