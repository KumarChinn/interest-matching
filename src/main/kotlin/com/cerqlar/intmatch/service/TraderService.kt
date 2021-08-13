package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.trader.Trader
import com.cerqlar.intmatch.model.trader.TraderDTO
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.EntityNotSavedException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.TraderMapper
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

/**
 * Created by chinnku on Aug, 2021
 * Service class for the Trader resource
 */
@Service
class TraderService(private val traderRepository: TraderRepository) {

    /**
     * Creates the Trader and returns the [TraderDTO]
     */
    fun createNewTrader(traderDTO: TraderDTO): TraderDTO {
        val trader = traderRepository.save(TraderMapper().toModel(traderDTO));
        if (ObjectUtils.isEmpty(trader)) {
            throw EntityNotSavedException("The provided Trader entity is not saved");
        }
        return TraderMapper().fromModel(trader)
    }

    /**
     * Finds the Trader by ID and returns the [TraderDTO]
     */
    fun findTraderById(traderId: Long): TraderDTO {
        val trader = traderRepository.findById(traderId);
        if (ObjectUtils.isEmpty(trader)) {
            throw ResourceNotFoundException("The resource Trader not found for id=$traderId");
        }
        return TraderMapper().fromModel(trader.get())
    }

    /**
     * Finds all the Interest resource and returns the collection of [TraderDTO]
     */
    fun getAllTraders(): Collection<TraderDTO> {
        val traderList = traderRepository.findAll();
        if (ObjectUtils.isEmpty(traderList)) {
            throw ResourceNotFoundException("There no resource Trader found");
        }
        return traderList.map { trader -> TraderMapper().fromModel(trader) }
    }

}