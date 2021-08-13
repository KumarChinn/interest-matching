package com.cerqlar.intmatch.utils.mapper

import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.interest.InterestDTO

/**
 * Created by chinnku on Aug, 2021
 * Mapper to convert from DTO to model and model to DTO
 * InterestMapper
 */
class InterestMapper : Mapper<InterestDTO, Interest> {
    override fun fromModel(model: Interest): InterestDTO = InterestDTO(
        model.intId,
        model.energySource,
        model.status!!,
        model.qty,
        model.intBuyer!!.traderId
    )
    override fun toModel(domain: InterestDTO): Interest = Interest(
        domain.intId,
        domain.energySource,
        domain.status,
        domain.qty,
        null
    )
}