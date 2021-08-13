package com.cerqlar.intmatch.model.bundle

import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.trader.Trader
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * Created by chinnku on Aug, 2021
 * CertificateBundle Model
 */
@Entity
@Table(name = "CERTIFICATE_BUNDLE")
data class CertificateBundle(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val cerBundleId: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    var seller: Trader?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuerId")
    var issuer: Trader?,
    val energySource: EnergySource,
    val qty: Long,
    var consumedQty: Long,
    val issuedDate: Date = Calendar.getInstance().time,
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinTable(
        name = "cerbundle_ints",
        joinColumns = [JoinColumn(name = "cerbun_id", referencedColumnName = "cerBundleId")],
        inverseJoinColumns = [JoinColumn(name = "int_id", referencedColumnName = "intId")]
    )
    @JsonIgnoreProperties("assigned_ints")
    var assignedInts: List<Interest> = mutableListOf()
)