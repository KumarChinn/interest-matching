package com.cerqlar.intmatch.model.interest

import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.trader.Trader
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

/**
 * Created by chinnku on Aug, 2021
 * Interest Model
 */
@Entity
@Table(name = "INTEREST")
data class Interest(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val intId: Long,
    val energySource: EnergySource,
    var status: InterestStatus?,
    val qty: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyerId")
    var intBuyer: Trader?,
    @ManyToMany(mappedBy = "assignedInts")
    @JsonIgnoreProperties("assignedInts")
    var cerBundles: List<CertificateBundle> = mutableListOf()
)