package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.repository.CertificateBundleRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(CertificateBundleService::class)
internal class CertificateBundleServiceTest {
    @MockkBean
    lateinit var mockCertificateBundleRepository: CertificateBundleRepository

    @MockkBean
    lateinit var mockTraderRepository: TraderRepository

    @Autowired
    private lateinit var certificateBundleService: CertificateBundleService

    @Test
    fun `when the cerbundle is loaded by id, the cerbundle is returned`() {
        val id=1L;
        val cerBundle = mockk<CertificateBundle>(relaxed = true)
        every { mockCertificateBundleRepository.findById(id) } returns Optional.of(cerBundle)

        val cerBudDTO = certificateBundleService.findCerBundleById(id)

        verify { mockCertificateBundleRepository.findById(id) }
        assertEquals(cerBundle.cerBundleId, cerBudDTO.cerBundleId)
    }

    @Test
    fun `when the trader is not found, there is a ResourceNotFoundException`() {
        val id=1L;
        every { mockCertificateBundleRepository.findById(id) } returns Optional.empty()

        org.junit.jupiter.api.assertThrows<ResourceNotFoundException> {
            certificateBundleService.findCerBundleById(id)
        }

        verify { mockCertificateBundleRepository.findById(id) }
    }

    @Test
    fun `when a new interest is created successfully, the interest info is returned`() {
        val mockCerBun = mockk<CertificateBundle>(relaxed = true)
        val mockTrader = mockk<Trader>(relaxed = true)
        val mockCerBunDTO = mockk<CertificateBundleDTO>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockTrader)
        every { mockCertificateBundleRepository.save(any()) } returns mockCerBun

        val newCerBunDTO = certificateBundleService.createNewCerBundle(mockCerBunDTO)

        verify { mockCertificateBundleRepository.save(any()) }
        verify { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) }
        assertEquals(mockCerBunDTO.cerBundleId, newCerBunDTO.cerBundleId)
    }

    @Test
    fun `when getting the trader list successfully, the list is returned`() {
        val mockTrader = mockk<Trader>(relaxed = true)
        val expectedCerBuns = listOf<CertificateBundle>(
            mockk<CertificateBundle>(relaxed = true),
            mockk<CertificateBundle>(relaxed = true)
        )
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockTrader)
        every { mockCertificateBundleRepository.findBySeller(any()) } returns expectedCerBuns

        val newCerBus = certificateBundleService.getAllCerBundlesBySeller(1L)

        verify { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) }
        verify { mockCertificateBundleRepository.findBySeller(any()) }
        assertEquals(newCerBus.size, expectedCerBuns.size)
    }

}