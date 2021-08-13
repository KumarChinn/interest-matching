package com.cerqlar.intmatch.utils.mapper

import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.model.trader.TraderDTO

/**
 * Created by chinnku on Aug, 2021
 * Mapper to convert from DTO to model and model to DTO
 * TraderMapper
 */
class TraderMapper : Mapper<TraderDTO, Trader> {
    override fun fromModel(model: Trader): TraderDTO = TraderDTO(
            model.traderId,
            model.traderRole,
            model.firstName,
            model.lastName,
            model.email,
            model.contact,
            model.tradingCompanyName)

    override fun toModel(domain: TraderDTO): Trader = Trader(
            domain.traderId,
            domain.traderRole,
            domain.firstName,
            domain.lastName,
            domain.email,
            domain.contact,
            domain.tradingCompanyName)
}