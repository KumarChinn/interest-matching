package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.dto.match.IntMatchingRequest
import com.cerqlar.intmatch.dto.match.IntMatchingResponse
import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.service.InterestMatchingService
import com.cerqlar.intmatch.service.TraderService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(InterestMatchingController::class)
internal class InterestMatchingControllerTest {
    @MockkBean
    lateinit var mockInterestMatchingService: InterestMatchingService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mockIntMatchingRequest = mockk<IntMatchingRequest>(relaxed = true) {
        every { sellerId } returns 1L
        every { intId } returns 2L
    }
    val intMatchJsonReq = ObjectMapper().writeValueAsString(mockIntMatchingRequest)

    @Test
    fun `when a seller and interest id is given to get, return the match`() {
        val mockIntMatchingResponse = mockk<IntMatchingResponse>(relaxed = true)
        every { mockInterestMatchingService.findMatchingCerBundles(mockIntMatchingRequest) } returns mockIntMatchingResponse
        val intMatchJson = ObjectMapper().writeValueAsString(mockIntMatchingResponse)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/intmatch-api/v1/int-match")
                .content(intMatchJsonReq)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(intMatchJson))
            .andReturn()

        verify { mockInterestMatchingService.findMatchingCerBundles(mockIntMatchingRequest) }
    }

    @Test
    fun `when a seller and interest id is given to assign, return the match`() {
        val mockIntMatchingResponse = mockk<IntMatchingResponse>(relaxed = true)
        every { mockInterestMatchingService.assignMatchingCerBundles(mockIntMatchingRequest) } returns mockIntMatchingResponse
        val intMatchJson = ObjectMapper().writeValueAsString(mockIntMatchingResponse)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/intmatch-api/v1/int-match")
                .content(intMatchJsonReq)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isAccepted)
            .andExpect(MockMvcResultMatchers.content().json(intMatchJson))
            .andReturn()

        verify { mockInterestMatchingService.assignMatchingCerBundles(mockIntMatchingRequest) }
    }

}