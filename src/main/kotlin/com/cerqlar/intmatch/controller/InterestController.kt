package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.interest.InterestDTO
import com.cerqlar.intmatch.service.InterestService
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
 * RestController for Interest resource
 */
@RestController
@RequestMapping("/intmatch-api/v1/interests")
@Api(value = "Interest", description = "REST API for Interest", tags = arrayOf("Interest"))
class InterestController(private val interestService: InterestService) {

    /**
     * PostMapping for creating the Interest resource
     */
    @PostMapping()
    @ApiOperation(
        value = "Create a Interest",
        notes = "Provide required information to create a Interest, Prerequisite: Buyer"
    )
    fun createInterest(@ApiParam(name = "Interest", value = "Interest Information")@RequestBody interestDTO: InterestDTO, uri: UriComponentsBuilder): ResponseEntity<InterestDTO> {
        val persistedInterestDTO = interestService.createNewInterest(interestDTO)
        if (ObjectUtils.isEmpty(interestDTO)) {
            throw EntityNotSavedException("The provided Interest entity is not saved");
        }
        return ResponseEntity<InterestDTO>(persistedInterestDTO, HttpStatus.CREATED)
    }

    /**
     * GetMapping for finding the Interest resource
     */
    @GetMapping("/{intId}")
    @ApiOperation(
        value = "Find Interest by Id",
        notes = "Provide the unique Interest Id to get the Interest"
    )
    fun fetchOrgById(@ApiParam(value = "ID value for the Interest you need to retrieve")@PathVariable("intId") intId: Long): ResponseEntity<InterestDTO> {
        val interestDTO = interestService.findInterestById(intId)
        if (ObjectUtils.isEmpty(interestDTO)) {
            throw ResourceNotFoundException("The resource Interest not found for id=$intId");
        }
        return ResponseEntity<InterestDTO>(interestDTO, HttpStatus.OK)
    }

    /**
     * GetMapping for finding all the Interest resource
     */
    @GetMapping()
    @ApiOperation(
        value = "Find all the Interests",
        notes = "Get all the available Interests"
    )
    fun getAllInterests(): ResponseEntity<Collection<InterestDTO>> {
        val intList = interestService.getAllInterests();
        if (ObjectUtils.isEmpty(intList)) {
            throw ResourceNotFoundException("There is no Interest resource found");
        }
        return ResponseEntity<Collection<InterestDTO>>(intList, HttpStatus.OK)
    }
}