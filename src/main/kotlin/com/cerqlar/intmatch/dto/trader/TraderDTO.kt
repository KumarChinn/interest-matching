package com.cerqlar.intmatch.model.trader

import com.cerqlar.intmatch.model.common.TraderRole
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Created by chinnku on Aug, 2021
 * TraderDTO
 */
@ApiModel(value = "Trader", description = "Details about the Trader")
data class TraderDTO(
    @ApiModelProperty(notes = "The unique Id of the Trader")
    val traderId: Long,
    @ApiModelProperty(notes = "The Role of the Trader, Example BUYER", example = "BUYER", required = true)
    val traderRole: TraderRole,
    @ApiModelProperty(notes = "The First Name of the Trader", example = "Kumar", required = true)
    val firstName: String,
    @ApiModelProperty(notes = "The Last Name of the Trader", example = "Chinnathambi", required = true)
    val lastName: String,
    @ApiModelProperty(notes = "The Email of the Trader", example = "kumarceg@cerqlar.com", required = true)
    val email: String,
    @ApiModelProperty(notes = "The Contact Number of the Trader", example = "062304823222")
    val contact: String?,
    @ApiModelProperty(notes = "The Trading Company Name", example = "CerQlar")
    val tradingCompanyName: String?,
)