package com.cerqlar.intmatch.model.trader

import com.cerqlar.intmatch.model.common.TraderRole
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

/**
 * Created by chinnku on Aug, 2021
 * Trader Model
 */
@Entity
@Table(name = "TRADER")
data class Trader(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    val traderId: Long,
    val traderRole: TraderRole,
    val firstName: String,
    val lastName: String,
    val email: String,
    val contact: String?,
    val tradingCompanyName: String?
)