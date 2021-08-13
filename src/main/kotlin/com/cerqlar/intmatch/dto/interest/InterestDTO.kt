package com.cerqlar.intmatch.model.interest

import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.InterestStatus
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Created by chinnku on Aug, 2021
 * InterestDTO
 */
@ApiModel(value = "Interest", description = "Details about the Interest")
data class InterestDTO(
    @ApiModelProperty(notes = "The unique Id of the Interest")
    val intId: Long,
    @ApiModelProperty(notes = "The energy source for the interest", required = true)
    val energySource: EnergySource,
    @ApiModelProperty(notes = "The status of the interest", required = true)
    val status: InterestStatus?,
    @ApiModelProperty(notes = "The quantity of the interest", required = true)
    val qty: Long,
    @ApiModelProperty(notes = "The buyer Id for the interest", required = true)
    val intBuyerId: Long
)