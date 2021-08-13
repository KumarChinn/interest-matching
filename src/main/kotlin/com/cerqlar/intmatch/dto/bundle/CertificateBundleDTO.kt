package com.cerqlar.intmatch.model.bundle

import com.cerqlar.intmatch.model.common.EnergySource
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.Date

/**
 * Created by chinnku on Aug, 2021
 * CertificateBundleDTO
 */
@ApiModel(value = "CertificateBundle", description = "Details about the Certificate Bundle")
data class CertificateBundleDTO(
    @ApiModelProperty(notes = "The unique Id of the CertificateBundle")
    val cerBundleId: Long,
    @ApiModelProperty(notes = "The unique Id of the Seller")
    var sellerId: Long,
    @ApiModelProperty(notes = "The unique Id of the Issuer")
    val issuerId: Long,
    @ApiModelProperty(notes = "The Energy Source of the Certificate Bundle")
    val energySource: EnergySource,
    @ApiModelProperty(notes = "The available Qty of the Certificate Bundle")
    val availableQty: Long,
    @ApiModelProperty(notes = "The actual Qty of the Certificate Bundle")
    val qty: Long,
    @ApiModelProperty(notes = "The consumed Qty of the Certificate Bundle")
    val consumedQty: Long,
    @ApiModelProperty(notes = "The issued date of the Certificate Bundle")
    val issuedDate: Date
)