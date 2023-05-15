package br.challenge.smartcashflowapp.controllers

import br.challenge.smartcashflowapp.configs.ObjectMapperConfig
import br.challenge.smartcashflowapp.controllers.exception.GlobalExceptionHandler
import br.challenge.smartcashflowapp.dtos.TransactionCreateRequestDto
import br.challenge.smartcashflowapp.dtos.TransactionDto
import br.challenge.smartcashflowapp.dtos.UpdatePaymentDateRequestDto
import br.challenge.smartcashflowapp.dtos.constants.*
import br.challenge.smartcashflowapp.entities.TransactionEntity
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapperConfig().objectMapper()

    @Test
    fun `should create transaction`() {
        val createRequestDto = TransactionCreateRequestDto(
            description = "Stock replenishment",
            amount = BigDecimal.valueOf(830.47),
            category = TransactionCategory.INVENTORY_COSTS,
            type = TransactionType.OUT,
            installmentsType = TransactionInstallmentType.MONTH,
            expectedPaymentDate = LocalDate.now(),
            paymentDate = LocalDate.now()
        )
        val transactionDto = TransactionDto(
            id = 1,
            relatedTransactionId = "a6151602-6cee-421a-bafd-1fa44f281dd9",
            currency = TransactionCurrency.BRL,
            description = createRequestDto.description,
            amount = createRequestDto.amount!!,
            category = createRequestDto.category!!,
            type = createRequestDto.type!!,
            expectedPaymentDate = createRequestDto.expectedPaymentDate!!,
            paymentDate = createRequestDto.paymentDate,
            status = TransactionStatus.PENDING
        )

        mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "description": "Stock replenishment",
                      "installmentsCount": 1,
                      "installmentsType": "MONTH",
                      "amount": 830.47,
                      "type": "OUT",
                      "category": "INVENTORY_COSTS",
                      "expectedPaymentDate": "2023-05-15",
                      "paymentDate": "2023-05-15"
                    }
                """.trimIndent())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(transactionDto.id))
            .andExpect(jsonPath("$[0].description").value(transactionDto.description))
            .andExpect(jsonPath("$[0].category").value(transactionDto.category.name))
            .andExpect(jsonPath("$[0].type").value(transactionDto.type.name))
            .andExpect(jsonPath("$[0].expectedPaymentDate").value(transactionDto.expectedPaymentDate.toString()))
            .andExpect(jsonPath("$[0].paymentDate").value(transactionDto.paymentDate.toString()))
    }

    @Test
    fun `when create is called then should return a bad request status`() {
        // Given
        val transactionCreateRequestDto = createInvalidTransactionCreateRequestDto()

        // When
        val result = mockMvc.perform(
            post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionCreateRequestDto))
        ).andExpect(status().isBadRequest).andReturn()

        // Then
        val response: GlobalExceptionHandler.ErrorResponse = objectMapper.readValue(result.response.contentAsString)

        assertNotNull(response)
        assertEquals("Validation failed", response.message)
        val sortedErrors = response.errors?.sortedBy { it.fieldName }

        assertEquals(6, sortedErrors?.size)
        assertEquals("amount", sortedErrors?.get(0)?.fieldName)
        assertEquals("this is a required field", sortedErrors?.get(0)?.message)
        assertEquals("category", sortedErrors?.get(1)?.fieldName)
        assertEquals(
            "this is a required field, the options are: SALES, UTILITIES, EMPLOYEE_SALARIES, INVENTORY_COSTS, MARKETING_EXPENSES, OTHER",
            sortedErrors?.get(1)?.message
        )
        assertEquals("description", sortedErrors?.get(2)?.fieldName)
        assertEquals("this is a required field", sortedErrors?.get(2)?.message)
        assertEquals("expectedPaymentDate", sortedErrors?.get(3)?.fieldName)
        assertEquals("this is a required field", sortedErrors?.get(3)?.message)
        assertEquals("installmentsCount", sortedErrors?.get(4)?.fieldName)
        assertEquals("this is a required field", sortedErrors?.get(4)?.message)
        assertEquals("type", sortedErrors?.get(5)?.fieldName)
        assertEquals("this is a required field, the options are: IN, OUT", sortedErrors?.get(5)?.message)
    }

    @Test
    fun `when update payment date is called then should no content success`() {
        // Given
        val transactionId = 2

        // When
        mockMvc.perform(
            put("/transactions/$transactionId/paymentDate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "paymentDate": "2023-05-15"
                    }
                """.trimIndent())
                .accept(MediaType.APPLICATION_JSON))

                // Then
            .andExpect(status().isNoContent)
    }

    private fun createInvalidTransactionCreateRequestDto() = TransactionCreateRequestDto(
        description = "",
        installmentsCount = null,
        amount = null,
        expectedPaymentDate = null,
        paymentDate = null,
        type = null,
        category = null
    )
}