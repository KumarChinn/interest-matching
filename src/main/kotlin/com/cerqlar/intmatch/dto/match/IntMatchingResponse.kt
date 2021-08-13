package com.cerqlar.intmatch.dto.match

import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.model.interest.InterestDTO
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * Created by chinnku on Aug, 2021
 * IntMatchingResponse
 */
@ApiModel(value = "IntMatchingResponse", description = "Details about the Interest Matching")
data class IntMatchingResponse(
    @ApiModelProperty(notes = "Details about the Interest used for matching")
    val interest: InterestDTO,
    @ApiModelProperty(notes = "List of Certificate Bundle considered for the matching")
    val assignedCerBundles: List<CertificateBundleDTO>
)