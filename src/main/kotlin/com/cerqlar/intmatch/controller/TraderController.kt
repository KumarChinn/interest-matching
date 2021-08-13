package com.cerqlar.intmatch.controller

import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.service.TraderService
import com.cerqlar.intmatch.utils.exception.EntityNotSavedException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.util.UriComponentsBuilder

/**
 * Created by chinnku on Aug, 2021
 * RestController for Trader resource
 */
@RestController
@RequestMapping("/intmatch-api/v1/traders")
@Api(value = "Trader", description = "REST API for Trader", tags = arrayOf("Trader"))
class TraderController(private val traderService: TraderService) {

    /**
     * PostMapping for creating the Trader resource
     */
    @PostMapping()
    @ApiOperation(
        value = "Create a Trader",
        notes = "Provide required information to create a Trader, A Trader can be SELLER/BUYER/ISSUER"
    )
    fun createTrader(
        @ApiParam(name = "Trader", value = "Trader Information") @RequestBody traderDTO: TraderDTO,
        uri: UriComponentsBuilder
    ): ResponseEntity<TraderDTO> {
        val persistedTraderDTO = traderService.createNewTrader(traderDTO)
        if (ObjectUtils.isEmpty(persistedTraderDTO)) {
            throw EntityNotSavedException("The provided Trader entity is not saved");
        }
        return ResponseEntity<TraderDTO>(persistedTraderDTO, HttpStatus.CREATED)
    }

    /**
     * GetMapping for findings the Trader resource
     */
    @GetMapping("/{traderId}")
    @ApiOperation(
        value = "Find Trader by Id",
        notes = "Provide the unique Trader Id to get the Trader"
    )
    fun fetchTraderById(@ApiParam(value = "ID value for the Trader you need to retrieve") @PathVariable("traderId") traderId: Long): ResponseEntity<TraderDTO> {
        val traderDTO = traderService.findTraderById(traderId)
        if (ObjectUtils.isEmpty(traderDTO)) {
            throw ResourceNotFoundException("The resource Trader not found for id=$traderId");
        }
        return ResponseEntity<TraderDTO>(traderDTO, HttpStatus.OK)
    }

    /**
     * GetMapping for finding all the Traders resource
     */
    @GetMapping()
    @ApiOperation(
        value = "Find all the Traders",
        notes = "Get all the available Traders"
    )
    fun getAllTraders(): ResponseEntity<Collection<TraderDTO>> {
        val tradersList = traderService.getAllTraders();
        if (ObjectUtils.isEmpty(tradersList)) {
            throw ResourceNotFoundException("There is no Trader resource found");
        }
        return ResponseEntity<Collection<TraderDTO>>(tradersList, HttpStatus.OK)
    }

}