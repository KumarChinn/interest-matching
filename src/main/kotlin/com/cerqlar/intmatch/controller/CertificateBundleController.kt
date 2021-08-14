package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.service.CertificateBundleService
import com.cerqlar.intmatch.utils.exception.EntityNotSavedException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

/**
 * Created by chinnku on Aug, 2021
 * RestController for CertificateBundle resource
 */
@RestController
@RequestMapping("/intmatch-api/v1/cerbundles")
@Api(
    value = "Certificate Bundle",
    description = "REST API for Certificate Bundle",
    tags = arrayOf("Certificate Bundle")
)
class CertificateBundleController(private val certificateBundleService: CertificateBundleService) {

    /**
     * PostMapping for creating the CertificateBundle resource
     */
    @PostMapping()
    @ApiOperation(
        value = "Create a Certificate Bundle",
        notes = "Provide required information to create a Certificate Bundle, Prerequisite: Seller, Issuer"
    )
    fun createCertificateBundle(
        @ApiParam(
            name = "CertificateBundle",
            value = "Certificate Bundle Information"
        ) @RequestBody certificateBundleDTO: CertificateBundleDTO,
        uri: UriComponentsBuilder
    ): ResponseEntity<CertificateBundleDTO> {
        val persistedCertificateBundleDTO = certificateBundleService.createNewCerBundle(certificateBundleDTO)
        if (ObjectUtils.isEmpty(certificateBundleDTO)) {
            throw EntityNotSavedException("The provided CertificateBundle entity is not saved");
        }
        return ResponseEntity<CertificateBundleDTO>(persistedCertificateBundleDTO, HttpStatus.CREATED)
    }

    /**
     * GetMapping for finding the CertificateBundle resource
     */
    @GetMapping("/{cerBundleId}")
    @ApiOperation(
        value = "Find Certificate Bundle by Id",
        notes = "Provide the unique Certificate Bundle Id to get the Certificate Bundle"
    )
    fun fetchCerBundleById(@ApiParam(value = "ID value for the Certificate Bundle you need to retrieve") @PathVariable("cerBundleId") cerBundleId: Long): ResponseEntity<CertificateBundleDTO> {
        val certificateBundleDTO = certificateBundleService.findCerBundleById(cerBundleId)
        if (ObjectUtils.isEmpty(certificateBundleDTO)) {
            throw ResourceNotFoundException("The resource Interest not found for id=$cerBundleId");
        }
        return ResponseEntity<CertificateBundleDTO>(certificateBundleDTO, HttpStatus.OK)
    }

    /**
     * GetMapping for finding all the CertificateBundle resource
     */
    @GetMapping("/cerbundle")
    @ApiOperation(
        value = "Find Certificate Bundles by Seller Id",
        notes = "Provide the unique Seller Id to get the associated Certificate Bundle"
    )
    fun getAllCerBundlesBySeller(@ApiParam(value = "Unique Seller Id")@RequestParam("sellerId") sellerId: Long): ResponseEntity<Collection<CertificateBundleDTO>> {
        val cerBunList = certificateBundleService.getAllCerBundlesBySeller(sellerId)
        if (ObjectUtils.isEmpty(cerBunList)) {
            throw ResourceNotFoundException("There is no CertificateBundle resource found");
        }
        return ResponseEntity<Collection<CertificateBundleDTO>>(cerBunList, HttpStatus.OK)
    }
}