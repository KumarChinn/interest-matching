package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.interest.InterestDTO
import com.cerqlar.intmatch.service.InterestService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(InterestController::class)
internal class InterestControllerTest {
    @MockkBean
    lateinit var mockInterestService: InterestService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mockInterestDTO = mockk<InterestDTO>(relaxed = true) {
        every { intId } returns 1L
    }
    val intJson = ObjectMapper().writeValueAsString(mockInterestDTO)

    fun getLogIn(): RequestPostProcessor {
        return SecurityMockMvcRequestPostProcessors.httpBasic("cerqlaradmin", "cerqlaradmin")
    }

    @Test
    fun `when a new interest created, there is an created response`() {
        every { mockInterestService.createNewInterest(any()) } returns mockInterestDTO

        mockMvc.perform(
            MockMvcRequestBuilders.post("/intmatch-api/v1/interests").with(getLogIn())
                .content(intJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().json(intJson))
            .andReturn()

        verify { mockInterestService.createNewInterest(any()) }
    }

    @Test
    fun `when a interest is get by Id, the interest should return`() {
        val id = 1L
        every { mockInterestService.findInterestById(id) } returns mockInterestDTO

        mockMvc.perform(
            MockMvcRequestBuilders.get("/intmatch-api/v1/interests/1").with(getLogIn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(intJson))
            .andReturn()

        verify { mockInterestService.findInterestById(id) }
    }

    @Test
    fun `when all interests is get, the interests list should return`() {
        val expectedInts = listOf<InterestDTO>(mockk<InterestDTO>(relaxed = true), mockk<InterestDTO>(relaxed = true))
        every { mockInterestService.getAllInterests() } returns expectedInts
        val tradersJson = ObjectMapper().writeValueAsString(expectedInts)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/intmatch-api/v1/interests").with(getLogIn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(tradersJson))
            .andReturn()

        verify { mockInterestService.getAllInterests() }
    }
}