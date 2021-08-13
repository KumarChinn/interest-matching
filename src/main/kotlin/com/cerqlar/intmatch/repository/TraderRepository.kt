package com.cerqlar.intmatch.repository

import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.trader.Trader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 * TraderRepository
 */
@Repository
interface TraderRepository : JpaRepository<Trader, Long> {
    /**
     * find Trader by Id and Role
     */
    fun findByTraderIdAndTraderRole(traderId: Long, traderRole: TraderRole): Optional<Trader>
}