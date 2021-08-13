package com.cerqlar.intmatch.dto.match

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Created by chinnku on Aug, 2021
 * IntMatchingRequest
 */
@ApiModel(value = "IntMatchingRequest", description = "Details about the Interest Matching")
data class IntMatchingRequest(
    @ApiModelProperty(notes = "The unique Id of the Seller")
    val sellerId: Long,
    @ApiModelProperty(notes = "The unique Id of the Interest")
    val intId: Long
)