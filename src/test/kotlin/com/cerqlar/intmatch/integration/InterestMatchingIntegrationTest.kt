package com.cerqlar.intmatch.integration

import com.cerqlar.intmatch.InterestMatchingApplication
import com.cerqlar.intmatch.dto.match.IntMatchingRequest
import com.cerqlar.intmatch.dto.match.IntMatchingResponse
import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.repository.CertificateBundleRepository
import com.cerqlar.intmatch.repository.InterestRepository
import com.cerqlar.intmatch.repository.TraderRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import java.net.URI
import java.util.*


/**
 * Created by chinnku on Aug, 2021
 */
@SpringBootTest(
    classes = arrayOf(InterestMatchingApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InterestMatchingIntegrationTest() {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var certificateBundleRepository: CertificateBundleRepository

    @Autowired
    private lateinit var interestRepository: InterestRepository

    @Autowired
    private lateinit var traderRepository: TraderRepository

    private var intMatchReq: IntMatchingRequest? = null
    private var buyer: Trader? = null
    private var seller: Trader? = null
    private var issuer: Trader? = null
    private var intOne: Interest? = null
    private var cerBunOne: CertificateBundle? = null
    private var cerBunTwo: CertificateBundle? = null

    @BeforeAll
    fun setUp() {
        restTemplate = TestRestTemplate("cerqlaradmin", "cerqlaradmin")

        buyer = Trader(1, TraderRole.BUYER, "Buyer5", "Chinna", "buyer5@cerqlar.com", "8213486123", "CerQlar")
        seller = Trader(2, TraderRole.SELLER, "Seller2", "Chinna", "seller2@cerqlar.com", "8213486123", "CerQlar")
        issuer = Trader(3, TraderRole.ISSUER, "Issuer2", "Chinna", "issuer2@cerqlar.com", "8213486123", "CerQlar")

        buyer = traderRepository.save(buyer!!)
        seller = traderRepository.save(seller!!)
        issuer = traderRepository.save(issuer!!)

        intOne = Interest(4, EnergySource.SOLAR, InterestStatus.OPEN, 300, buyer, emptyList())

        intOne = interestRepository.save(intOne!!)

        cerBunOne = CertificateBundle(5, seller, issuer, EnergySource.SOLAR, 100, 0, Calendar.getInstance().time)
        cerBunTwo = CertificateBundle(6, seller, issuer, EnergySource.SOLAR, 200, 0, Calendar.getInstance().time)

        cerBunOne = certificateBundleRepository.save(cerBunOne!!);
        cerBunTwo = certificateBundleRepository.save(cerBunTwo!!);

        intMatchReq = IntMatchingRequest(seller!!.traderId, intOne!!.intId)
    }

    @AfterAll
    fun cleanup() {
        val copiedCErBunOne = cerBunOne!!.copy(assignedInts = emptyList())
        val copiedCErBunTwo = cerBunTwo!!.copy(assignedInts = emptyList())
        certificateBundleRepository.save(copiedCErBunOne);
        certificateBundleRepository.save(copiedCErBunTwo);
        certificateBundleRepository.delete(copiedCErBunOne)
        certificateBundleRepository.delete(copiedCErBunTwo)

        val copiedIntOne = intOne!!.copy(cerBundles = emptyList())
        interestRepository.save(copiedIntOne)
        interestRepository.delete(copiedIntOne)

        traderRepository.delete(buyer!!)
        traderRepository.delete(seller!!)
        traderRepository.delete(issuer!!)

        intMatchReq = null
        buyer = null
        seller = null
        issuer = null
        intOne = null
        cerBunOne = null
        cerBunTwo = null
    }

    @Test
    fun `when match requested for a int, match returned`() {
        val responseEntity: ResponseEntity<IntMatchingResponse> = restTemplate
            .postForEntity(
                "http://localhost:$port/intmatch-api/v1/int-match",
                intMatchReq,
                IntMatchingResponse::class.java
            )
        val intMatchingResponse: IntMatchingResponse? = responseEntity.body
        assertEquals(202, responseEntity.statusCodeValue)
        assertEquals(InterestStatus.CLOSED, intMatchingResponse!!.interest.status)
        assertEquals(intMatchingResponse!!.assignedCerBundles.size, 2)
        val filterList = intMatchingResponse.assignedCerBundles.filter { it.availableQty == 0L }
        assertEquals(2, filterList.size)
    }
}