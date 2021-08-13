package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.dto.match.IntMatchingRequest
import com.cerqlar.intmatch.dto.match.IntMatchingResponse
import com.cerqlar.intmatch.service.InterestMatchingService
import com.cerqlar.intmatch.utils.exception.NoMatchFoundException
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
 * RestController for Interest Matching
 */
@RestController
@RequestMapping("/intmatch-api/v1/int-match")
@Api(value = "Interest Matching", description = "REST API for Interest Matching", tags = arrayOf("Interest Matching"))
class InterestMatchingController(private val interestMatchingService: InterestMatchingService) {

    /**
     * GetMapping for findings the matching for the interest.
     * This does not assign and close the interest
     */
    @GetMapping()
    @ApiOperation(
        value = "Find Matching for a Interest",
        notes = "This finds the Certificate Bundle matches for the given interest and seller, This does not assign the match."
    )
    fun findMatchingCerBundles(
        @ApiParam(name = "IntMatchingRequest", value = "Information to find the match")
        @RequestBody intMatchingRequest: IntMatchingRequest,
        uri: UriComponentsBuilder
    ): ResponseEntity<IntMatchingResponse> {
        val intMatchResponse = interestMatchingService.findMatchingCerBundles(intMatchingRequest)
        if (ObjectUtils.isEmpty(intMatchResponse)) {
            throw NoMatchFoundException("There is no match found for the given Interest");
        }
        return ResponseEntity<IntMatchingResponse>(intMatchResponse, HttpStatus.OK)
    }

    /**
     * PostMapping for findings the matching for the interest.
     * This also closes the Interest and CerBundles
     */
    @PostMapping()
    @ApiOperation(
        value = "Assign Matching for a Interest",
        notes = "This finds the Certificate Bundle matches for the given interest and seller, and assigns the match."
    )
    fun assignMatchingCerBundles(
        @ApiParam(name = "IntMatchingRequest", value = "Information to find and assign the match")
        @RequestBody intMatchingRequest: IntMatchingRequest,
        uri: UriComponentsBuilder
    ): ResponseEntity<IntMatchingResponse> {
        val intMatchResponse = interestMatchingService.assignMatchingCerBundles(intMatchingRequest)
        if (ObjectUtils.isEmpty(intMatchResponse)) {
            throw NoMatchFoundException("There is no match found for the given Interest");
        }
        return ResponseEntity<IntMatchingResponse>(intMatchResponse, HttpStatus.ACCEPTED)
    }
}