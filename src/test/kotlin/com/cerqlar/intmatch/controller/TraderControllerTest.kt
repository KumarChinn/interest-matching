package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.service.TraderService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.RequestPostProcessor


/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(TraderController::class)
internal class TraderControllerTest {
    @MockkBean
    lateinit var mockTraderService: TraderService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mockTraderDTO = mockk<TraderDTO>(relaxed = true) {
        every { traderId } returns 1L
    }
    val traderJson = ObjectMapper().writeValueAsString(mockTraderDTO)

    fun getLogIn(): RequestPostProcessor {
        return httpBasic("cerqlaradmin", "cerqlaradmin")
    }

    @Test
    @Throws(Exception::class)
    fun `when no auth provided, the access is protected`() {
        mockMvc.perform(get("/")).andExpect(status().isUnauthorized).andReturn()
    }

    @Test
    fun `when a new trader created, there is an ok response`() {
        every { mockTraderService.createNewTrader(any()) } returns mockTraderDTO

        mockMvc.perform(
            MockMvcRequestBuilders.post("/intmatch-api/v1/traders").with(getLogIn())
                .content(traderJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated)
            .andExpect(content().json(traderJson))
            .andReturn()

        verify { mockTraderService.createNewTrader(any()) }
    }

    @Test
    fun `when a trader is get by Id, the trader should return`() {
        val id = 1L
        every { mockTraderService.findTraderById(id) } returns mockTraderDTO

        mockMvc.perform(
            get("/intmatch-api/v1/traders/1").with(getLogIn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().json(traderJson))
            .andReturn()

        verify { mockTraderService.findTraderById(id) }
    }

    @Test
    fun `when all traders is get, the traders list should return`() {
        val expectedTraders = listOf<TraderDTO>(mockk<TraderDTO>(relaxed = true), mockk<TraderDTO>(relaxed = true))
        every { mockTraderService.getAllTraders() } returns expectedTraders
        val tradersJson = ObjectMapper().writeValueAsString(expectedTraders)

        mockMvc.perform(
            get("/intmatch-api/v1/traders").with(getLogIn())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(content().json(tradersJson))
            .andReturn()

        verify { mockTraderService.getAllTraders() }
    }

}