package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.service.CertificateBundleService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 */
@ExtendWith(SpringExtension::class)
@WebMvcTest(CertificateBundleController::class)
internal class CertificateBundleControllerTest {

    @MockkBean
    lateinit var mockCertificateBundleService: CertificateBundleService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mockCerBundleDTO = mockk<CertificateBundleDTO>(relaxed = true) {
        every { cerBundleId } returns 1L
        every { issuedDate } returns Calendar.getInstance().time
    }
    val cerBunJson = ObjectMapper().writeValueAsString(mockCerBundleDTO)

    @Test
    fun `when a new cerbundle created, there is an created response`() {
        every { mockCertificateBundleService.createNewCerBundle(any()) } returns mockCerBundleDTO

        mockMvc.perform(
            MockMvcRequestBuilders.post("/intmatch-api/v1/cerbundles")
                .content(cerBunJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        verify { mockCertificateBundleService.createNewCerBundle(any()) }
    }

    @Test
    fun `when a cerbundles is get by Id, the cerbundles should return`() {
        val id = 1L
        every { mockCertificateBundleService.findCerBundleById(id) } returns mockCerBundleDTO

        mockMvc.perform(
            MockMvcRequestBuilders.get("/intmatch-api/v1/cerbundles/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify { mockCertificateBundleService.findCerBundleById(id) }
    }

    @Test
    fun `when all cerbundles is get by Seller Id, the cerbundles list should return`() {
        val expectedCerBundles = listOf<CertificateBundleDTO>(
            mockk<CertificateBundleDTO>(relaxed = true),
            mockk<CertificateBundleDTO>(relaxed = true)
        )
        every { mockCertificateBundleService.getAllCerBundlesBySeller(1L) } returns expectedCerBundles

        mockMvc.perform(
            MockMvcRequestBuilders.get("/intmatch-api/v1/cerbundles/cerbundle")
                .param("sellerId", "1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify { mockCertificateBundleService.getAllCerBundlesBySeller(1L) }
    }

}