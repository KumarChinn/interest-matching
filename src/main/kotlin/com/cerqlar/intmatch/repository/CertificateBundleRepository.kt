package com.cerqlar.intmatch.repository

import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.common.EnergySource
import com.cerqlar.intmatch.model.trader.Trader
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Created by chinnku on Aug, 2021
 * CertificateBundleRepository
 */
@Repository
interface CertificateBundleRepository : JpaRepository<CertificateBundle, Long> {
    /**
     * Find CertificateBundle by Seller
     */
    fun findBySeller(seller: Trader): List<CertificateBundle>

    /**
     * Find CertificateBundle by Seller and Energy Source
     */
    fun findBySellerAndEnergySource(seller: Trader, energySource: EnergySource): List<CertificateBundle>
}