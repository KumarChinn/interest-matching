package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.interest.InterestDTO
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.repository.InterestRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.TraderMapper
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(InterestService::class)
internal class InterestServiceTest {
    @MockkBean
    lateinit var mockInterestRepository: InterestRepository

    @MockkBean
    lateinit var mockTraderRepository: TraderRepository

    @Autowired
    private lateinit var interestService: InterestService

    @Test
    fun `when the interest is loaded by id, the trader is returned`() {
        val mockInterest = mockk<Interest>(relaxed = true)
        val intId = 1L;
        every { mockInterestRepository.findById(intId) } returns Optional.of(mockInterest)

        val intDTO = interestService.findInterestById(intId)

        verify { mockInterestRepository.findById(intId) }
        assertEquals(mockInterest.intId, intDTO.intId)
    }

    @Test
    fun `when the interest is not found, there is a ResourceNotFoundException`() {
        val intId = 1L;
        every { mockInterestRepository.findById(intId) } returns Optional.empty()

        org.junit.jupiter.api.assertThrows<ResourceNotFoundException> {
            interestService.findInterestById(intId)
        }

        verify { mockInterestRepository.findById(intId) }
    }

    @Test
    fun `when a interest is created for the invalid buyer, there is a Exception`() {
        val mockInterestDTO = mockk<InterestDTO>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.empty()

        org.junit.jupiter.api.assertThrows<ResourceNotFoundException> {
            interestService.createNewInterest(mockInterestDTO)
        }

        verify { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) }
    }

    @Test
    fun `when a new interest is created successfully, the interest info is returned`() {
        val mockInterest = mockk<Interest>(relaxed = true)
        val mockTrader = mockk<Trader>(relaxed = true)
        val mockInterestDTO = mockk<InterestDTO>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockTrader)
        every { mockInterestRepository.save(any()) } returns mockInterest

        val newIntDTO = interestService.createNewInterest(mockInterestDTO)

        verify { mockInterestRepository.save(any()) }
        verify { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) }
        assertEquals(mockInterestDTO.intId, newIntDTO.intId)
    }
}