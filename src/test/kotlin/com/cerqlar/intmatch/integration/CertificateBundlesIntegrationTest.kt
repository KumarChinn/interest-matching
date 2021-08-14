package com.cerqlar.intmatch.integration

import com.cerqlar.intmatch.InterestMatchingApplication
import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.repository.CertificateBundleRepository
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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*


/**
 * Created by chinnku on Aug, 2021
 */
@SpringBootTest(
    classes = arrayOf(InterestMatchingApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CertificateBundlesIntegrationTest() {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var certificateBundleRepository: CertificateBundleRepository

    @Autowired
    private lateinit var traderRepository: TraderRepository

    private var cerBunDTO: CertificateBundleDTO? = null
    private var seller: Trader? = null
    private var issuer: Trader? = null
    private var cerBunOne: CertificateBundle? = null

    @BeforeAll
    fun setUp() {
        restTemplate = TestRestTemplate("cerqlaradmin", "cerqlaradmin")

        seller = Trader(1, TraderRole.SELLER, "Seller1", "Chinna", "seller1@cerqlar.com", "8213486123", "CerQlar")
        issuer = Trader(2, TraderRole.ISSUER, "Issuer1", "Chinna", "issuer1@cerqlar.com", "8213486123", "CerQlar")

        seller = traderRepository.save(seller!!)
        issuer = traderRepository.save(issuer!!)

        cerBunDTO = CertificateBundleDTO(
            5,
            seller!!.traderId,
            issuer!!.traderId,
            EnergySource.SOLAR,
            0,
            100,
            0,
            Calendar.getInstance().time
        )
        cerBunOne = CertificateBundle(3, seller!!, issuer!!, EnergySource.SOLAR, 100, 0, Calendar.getInstance().time)
        
        cerBunOne = certificateBundleRepository.save(cerBunOne!!);
    }

    @AfterAll
    fun cleanup() {
        certificateBundleRepository.delete(cerBunOne!!)
        traderRepository.delete(seller!!)
        traderRepository.delete(issuer!!)
        cerBunDTO = null
        seller = null
        issuer = null
        cerBunOne = null
    }

    @Test
    fun `when cerbun is posted, its created sucessfully`() {
        val responseEntity: ResponseEntity<CertificateBundleDTO> = restTemplate
            .postForEntity(
                "http://localhost:$port/intmatch-api/v1/cerbundles",
                cerBunDTO,
                CertificateBundleDTO::class.java
            )
        val resCertificateBundleDTO: CertificateBundleDTO? = responseEntity.body

        val entity = certificateBundleRepository.findById(resCertificateBundleDTO!!.cerBundleId)
        certificateBundleRepository.delete(entity.get());

        assertEquals(201, responseEntity.statusCodeValue)
        assertEquals(resCertificateBundleDTO!!.qty, 100L)
    }

    @Test
    fun `when cerbun is get by Id, the cerbun should return `() {
        val responseEntity: ResponseEntity<CertificateBundleDTO> = restTemplate.getForEntity<CertificateBundleDTO>(
            "http://localhost:$port/intmatch-api/v1/cerbundles/${cerBunOne!!.cerBundleId}",
            CertificateBundleDTO::class.java
        )

        val resCertificateBundleDTO: CertificateBundleDTO? = responseEntity.body
        assertEquals(responseEntity.statusCode, HttpStatus.OK)
        assertEquals(resCertificateBundleDTO!!.qty, 100)
    }
}