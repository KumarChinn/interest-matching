package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.dto.match.IntMatchingRequest
import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.repository.CertificateBundleRepository
import com.cerqlar.intmatch.repository.InterestRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.NoMatchFoundException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
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
@WebMvcTest(InterestMatchingService::class)
internal class InterestMatchingServiceTest {
    @MockkBean
    lateinit var mockTraderRepository: TraderRepository

    @MockkBean
    lateinit var mockInterestRepository: InterestRepository

    @MockkBean
    lateinit var mockCertificateBundleRepository: CertificateBundleRepository

    @Autowired
    private lateinit var interestMatchingService: InterestMatchingService

    @Test
    fun `when exact match (one) found for int, then the int is closed and cerbun qty consumed`() {

        val mockIntMatchReq = mockk<IntMatchingRequest>(relaxed = true)

        val mockSeller = mockk<Trader>(relaxed = true)
        val interest = Interest(1L, EnergySource.SOLAR, InterestStatus.OPEN, 300L, mockSeller, emptyList())
        val mockCerBunList = listOf<CertificateBundle>(
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                300L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            )
        )
        val mockCerbun = mockk<CertificateBundle>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockSeller)
        every { mockInterestRepository.findByintIdAndStatus(any(), any()) } returns Optional.of(interest)
        every {
            mockCertificateBundleRepository.findBySellerAndEnergySource(
                mockSeller,
                interest.energySource
            )
        } returns mockCerBunList
        every { mockCertificateBundleRepository.save(any()) } returns mockCerbun
        every { mockInterestRepository.save(any()) } returns interest

        val intMatchRes = interestMatchingService.assignMatchingCerBundles(mockIntMatchReq)

        val filterList = intMatchRes.assignedCerBundles.filter { it.availableQty == 0L }
        assertEquals(InterestStatus.CLOSED, intMatchRes.interest.status)
        assertEquals(1, filterList.size)
    }

    @Test
    fun `when exact match (more than one) found for int, then the int is closed and cerbun qty consumed`() {

        val mockIntMatchReq = mockk<IntMatchingRequest>(relaxed = true)

        val mockSeller = mockk<Trader>(relaxed = true)
        val interest = Interest(1L, EnergySource.SOLAR, InterestStatus.OPEN, 300L, mockSeller, emptyList())
        val mockCerBunList = listOf<CertificateBundle>(
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                200L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                50L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            )
        )
        val mockCerbun = mockk<CertificateBundle>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockSeller)
        every { mockInterestRepository.findByintIdAndStatus(any(), any()) } returns Optional.of(interest)
        every {
            mockCertificateBundleRepository.findBySellerAndEnergySource(
                mockSeller,
                interest.energySource
            )
        } returns mockCerBunList
        every { mockCertificateBundleRepository.save(any()) } returns mockCerbun
        every { mockInterestRepository.save(any()) } returns interest

        val intMatchRes = interestMatchingService.assignMatchingCerBundles(mockIntMatchReq)

        val filterList = intMatchRes.assignedCerBundles.filter { it.availableQty == 0L }
        assertEquals(InterestStatus.CLOSED, intMatchRes.interest.status)
        assertEquals(2, filterList.size)
    }

    @Test
    fun `when one match (not exact) found for int, then the int is closed and cerbun qty consumed`() {

        val mockIntMatchReq = mockk<IntMatchingRequest>(relaxed = true)

        val mockSeller = mockk<Trader>(relaxed = true)
        val interest = Interest(1L, EnergySource.SOLAR, InterestStatus.OPEN, 300L, mockSeller, emptyList())
        val mockCerBunList = listOf<CertificateBundle>(
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                400L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            )
        )
        val mockCerbun = mockk<CertificateBundle>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockSeller)
        every { mockInterestRepository.findByintIdAndStatus(any(), any()) } returns Optional.of(interest)
        every {
            mockCertificateBundleRepository.findBySellerAndEnergySource(
                mockSeller,
                interest.energySource
            )
        } returns mockCerBunList
        every { mockCertificateBundleRepository.save(any()) } returns mockCerbun
        every { mockInterestRepository.save(any()) } returns interest

        val intMatchRes = interestMatchingService.assignMatchingCerBundles(mockIntMatchReq)

        val filterList = intMatchRes.assignedCerBundles.filter { it.availableQty == 100L }
        assertEquals(InterestStatus.CLOSED, intMatchRes.interest.status)
        assertEquals(1, filterList.size)
    }

    @Test
    fun `when match found for int with combination, then the int is closed and cerbun qty consumed`() {

        val mockIntMatchReq = mockk<IntMatchingRequest>(relaxed = true)

        val mockSeller = mockk<Trader>(relaxed = true)
        val interest = Interest(1L, EnergySource.SOLAR, InterestStatus.OPEN, 300L, mockSeller, emptyList())
        val mockCerBunList = listOf<CertificateBundle>(
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                400L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            )
        )
        val mockCerbun = mockk<CertificateBundle>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockSeller)
        every { mockInterestRepository.findByintIdAndStatus(any(), any()) } returns Optional.of(interest)
        every {
            mockCertificateBundleRepository.findBySellerAndEnergySource(
                mockSeller,
                interest.energySource
            )
        } returns mockCerBunList
        every { mockCertificateBundleRepository.save(any()) } returns mockCerbun
        every { mockInterestRepository.save(any()) } returns interest

        val intMatchRes = interestMatchingService.assignMatchingCerBundles(mockIntMatchReq)

        val filterList = intMatchRes.assignedCerBundles.filter { it.availableQty == 100L }
        assertEquals(InterestStatus.CLOSED, intMatchRes.interest.status)
        assertEquals(1, filterList.size)
    }

    @Test
    fun `when no match found for int, then there is NoMatchFoundException`() {

        val mockIntMatchReq = mockk<IntMatchingRequest>(relaxed = true)

        val mockSeller = mockk<Trader>(relaxed = true)
        val interest = Interest(1L, EnergySource.SOLAR, InterestStatus.OPEN, 300L, mockSeller, emptyList())
        val mockCerBunList = listOf<CertificateBundle>(
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            ),
            CertificateBundle(
                1L,
                mockSeller,
                mockSeller,
                EnergySource.SOLAR,
                100L,
                0L,
                Calendar.getInstance().time,
                emptyList()
            )
        )
        val mockCerbun = mockk<CertificateBundle>(relaxed = true)
        every { mockTraderRepository.findByTraderIdAndTraderRole(any(), any()) } returns Optional.of(mockSeller)
        every { mockInterestRepository.findByintIdAndStatus(any(), any()) } returns Optional.of(interest)
        every {
            mockCertificateBundleRepository.findBySellerAndEnergySource(
                mockSeller,
                interest.energySource
            )
        } returns mockCerBunList
        every { mockCertificateBundleRepository.save(any()) } returns mockCerbun
        every { mockInterestRepository.save(any()) } returns interest

        org.junit.jupiter.api.assertThrows<NoMatchFoundException> {
            val intMatchRes = interestMatchingService.assignMatchingCerBundles(mockIntMatchReq)
        }
    }

}