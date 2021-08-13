package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.TraderMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(TraderService::class)
internal class TraderServiceTest {
    @MockkBean
    lateinit var mockTraderRepository: TraderRepository

    @Autowired
    private lateinit var traderService: TraderService

    @Test
    fun `when the trader is loaded by id, the trader is returned`() {
        val id=1L
        val expectedTrader = mockk<Trader>(relaxed = true)
        every { mockTraderRepository.findById(id) } returns Optional.of(expectedTrader)

        val traderDTO = traderService.findTraderById(id)

        verify { mockTraderRepository.findById(id) }
        assertEquals(expectedTrader.traderId, traderDTO.traderId)
    }

    @Test
    fun `when the trader is not found, there is a ResourceNotFoundException`() {
        val id=1L
        every { mockTraderRepository.findById(id) } returns Optional.empty()

        assertThrows<ResourceNotFoundException> {
            traderService.findTraderById(id)
        }

        verify { mockTraderRepository.findById(id) }
    }

    @Test
    fun `when a new trader is created successfully, the trader info is returned`() {
        val id=1L
        val traderDTO = mockk<TraderDTO>(relaxed = true) {
            every { traderId } returns id
        }
        every { mockTraderRepository.save(any()) } returns TraderMapper().toModel(traderDTO)

        val newTraderDTO = traderService.createNewTrader(traderDTO)

        verify { mockTraderRepository.save(any()) }
        assertEquals(traderDTO.traderId, newTraderDTO.traderId)
    }

    @Test
    fun `when getting the trader list successfully, the list is returned`() {
        val expectedTraders = listOf<Trader>(mockk<Trader>(relaxed = true), mockk<Trader>(relaxed = true))
        every { mockTraderRepository.findAll() } returns expectedTraders

        val traders = traderService.getAllTraders()

        verify { mockTraderRepository.findAll() }
        assertEquals(expectedTraders.size, traders.size)
    }
}