package com.cerqlar.intmatch.integration

import com.cerqlar.intmatch.InterestMatchingApplication
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.model.trader.TraderDTO
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
import org.springframework.test.annotation.DirtiesContext
import java.net.URI

/**
 * Created by chinnku on Aug, 2021
 */
@SpringBootTest(
    classes = arrayOf(InterestMatchingApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TraderIntegrationTest() {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var traderRepository: TraderRepository

    private var traderDTO: TraderDTO? = null
    private var traderOne: Trader? = null
    private var traderTwo: Trader? = null

    @BeforeAll
    fun setUp() {
        restTemplate = TestRestTemplate("cerqlaradmin", "cerqlaradmin")
        traderDTO = TraderDTO(1L, TraderRole.BUYER, "Buyer1", "Chinna", "buyer1@cerqlar.com", "8213486123", "CerQlar")
        traderOne = Trader(1L, TraderRole.BUYER, "Buyer1", "Chinna", "buyer1@cerqlar.com", "8213486123", "CerQlar")
        traderTwo = Trader(2L, TraderRole.BUYER, "Buyer3", "Chinna", "buyer3@cerqlar.com", "8213486123", "CerQlar")

        traderOne = traderRepository.save(traderOne!!);
        traderTwo = traderRepository.save(traderTwo!!);
    }

    @AfterAll
    fun cleanup() {
        traderRepository.delete(traderOne!!)
        traderRepository.delete(traderTwo!!)
        traderDTO = null
        traderOne = null
        traderTwo = null
    }

    @Test
    fun `when trader posted, its created sucessfully`() {
        val responseEntity: ResponseEntity<TraderDTO> = restTemplate
            .postForEntity("http://localhost:$port/intmatch-api/v1/traders", traderDTO, TraderDTO::class.java)

        val resTraderDTO: TraderDTO? = responseEntity.body

        val entity = traderRepository.findById(resTraderDTO!!.traderId)
        traderRepository.delete(entity.get());

        assertEquals(201, responseEntity.statusCodeValue)
        assertEquals(resTraderDTO!!.firstName, "Buyer1")
    }

    @Test
    fun `when trader is get by Id, the trader should return `() {
        val responseEntity: ResponseEntity<TraderDTO> = restTemplate.getForEntity<TraderDTO>(
            "http://localhost:$port/intmatch-api/v1/traders/${traderOne!!.traderId}",
            TraderDTO::class.java
        )

        val resTraderDTO: TraderDTO? = responseEntity.body
        assertEquals(responseEntity.statusCode, HttpStatus.OK)
        assertEquals(resTraderDTO!!.email, "buyer1@cerqlar.com")
    }

    @Test
    fun `when traders is get, the trader list should return `() {
        val endpoint = URI.create("http://localhost:$port/intmatch-api/v1/traders")
        val request = RequestEntity<Any>(HttpMethod.GET, endpoint)
        val respType = object : ParameterizedTypeReference<Collection<TraderDTO>>() {}

        val responseEntity: ResponseEntity<Collection<TraderDTO>> = restTemplate.exchange(request, respType)

        val resTraderDTOList: Collection<TraderDTO>? = responseEntity.body
        assertEquals(responseEntity.statusCode, HttpStatus.OK)
        assertEquals(resTraderDTOList!!.size, 2)
    }
}
