package com.cerqlar.intmatch.integration

import com.cerqlar.intmatch.InterestMatchingApplication
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.interest.InterestDTO
import com.cerqlar.intmatch.model.trader.Trader
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
class InterestIntegrationTest() {

    @LocalServerPort
    private val port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var interestRepository: InterestRepository

    @Autowired
    private lateinit var traderRepository: TraderRepository

    private var intDTO: InterestDTO? = null
    private var trader: Trader? = null
    private var intOne: Interest? = null
    private var intTwo: Interest? = null

    @BeforeAll
    fun setUp() {
        restTemplate = TestRestTemplate("cerqlaradmin", "cerqlaradmin")
        trader = Trader(4, TraderRole.BUYER, "Buyer4", "Chinna", "buyer4@cerqlar.com", "8213486123", "CerQlar")
        trader = traderRepository.save(trader!!)

        intOne = Interest(4, EnergySource.SOLAR, InterestStatus.OPEN, 100L, trader, emptyList())
        intTwo = Interest(5, EnergySource.SOLAR, InterestStatus.OPEN, 100L, trader, emptyList())
        intDTO = InterestDTO(6, EnergySource.SOLAR, InterestStatus.OPEN, 100L, trader!!.traderId)

        intOne = interestRepository.save(intOne!!);
        intTwo = interestRepository.save(intTwo!!);
    }

    @AfterAll
    fun cleanup() {
        interestRepository.delete(intOne!!)
        interestRepository.delete(intTwo!!)
        traderRepository.delete(trader!!)

        intDTO = null
        trader = null
        intOne = null
        intTwo = null
    }

    @Test
    fun `when interest is posted, its created sucessfully`() {
        val responseEntity: ResponseEntity<InterestDTO> = restTemplate
            .postForEntity("http://localhost:$port/intmatch-api/v1/interests", intDTO, InterestDTO::class.java)
        val resIntDTO: InterestDTO? = responseEntity.body

        val int = interestRepository.findById(resIntDTO!!.intId)
        interestRepository.delete(int.get());

        assertEquals(201, responseEntity.statusCodeValue)
        assertEquals(resIntDTO!!.status, InterestStatus.OPEN)
    }

    @Test
    fun `when interest is get by Id, the interest should return `() {
        val responseEntity: ResponseEntity<InterestDTO> = restTemplate.getForEntity<InterestDTO>(
            "http://localhost:$port/intmatch-api/v1/interests/${intOne!!.intId}",
            InterestDTO::class.java
        )

        val resInterestDTO: InterestDTO? = responseEntity.body
        assertEquals(responseEntity.statusCode, HttpStatus.OK)
        assertEquals(resInterestDTO!!.status, InterestStatus.OPEN)
    }

    @Test
    fun `when interest is get, the interest list should return `() {
        val endpoint = URI.create("http://localhost:$port/intmatch-api/v1/interests")
        val request = RequestEntity<Any>(HttpMethod.GET, endpoint)
        val respType = object : ParameterizedTypeReference<Collection<InterestDTO>>() {}

        val responseEntity: ResponseEntity<Collection<InterestDTO>> = restTemplate.exchange(request, respType)

        val resInterestDTOList: Collection<InterestDTO>? = responseEntity.body
        assertEquals(responseEntity.statusCode, HttpStatus.OK)
        assertEquals(resInterestDTOList!!.size, 2)
    }
}