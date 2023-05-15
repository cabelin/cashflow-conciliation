package br.challenge.smartcashflowapp.controllers

import br.challenge.smartcashflowapp.dtos.TransactionCreateRequestDto
import br.challenge.smartcashflowapp.dtos.TransactionDto
import br.challenge.smartcashflowapp.dtos.TransactionReportResponseDto
import br.challenge.smartcashflowapp.dtos.constants.TransactionReportType
import br.challenge.smartcashflowapp.services.TransactionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/transactions")
class TransactionController(private val transactionService: TransactionService) {
    @PostMapping
    fun create(@RequestBody @Valid createRequestDto: TransactionCreateRequestDto): ResponseEntity<List<TransactionDto>> {
        return ResponseEntity.ok(transactionService.createTransaction(createRequestDto))
    }

    @GetMapping
    fun retrieveAll(@PageableDefault pageable: Pageable): ResponseEntity<Page<TransactionDto>> {
        val transactions = transactionService.findAllTransactions(pageable)
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/reports")
    fun retrieveReport(
        @RequestParam(value = "type")
        type: TransactionReportType? = null,

        @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): ResponseEntity<TransactionReportResponseDto> {
        return ResponseEntity.ok(
            transactionService.report(type, date)
        )
    }
}