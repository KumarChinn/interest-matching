package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.repository.CertificateBundleRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.EntityNotSavedException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.CertificateBundleMapper
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

/**
 * Created by chinnku on Aug, 2021
 * Service class for the CertificateBundle resource
 */
@Service
class CertificateBundleService(
    private val certificateBundleRepository: CertificateBundleRepository,
    private val traderRepository: TraderRepository
) {

    /**
     * Creates the CertificateBundle and returns the [CertificateBundleDTO]
     */
    fun createNewCerBundle(cerBundleDTO: CertificateBundleDTO): CertificateBundleDTO {
        val seller = traderRepository.findByTraderIdAndTraderRole(cerBundleDTO.sellerId, TraderRole.SELLER)
        if (ObjectUtils.isEmpty(seller)) {
            throw ResourceNotFoundException("Could not create CertificateBundle, There is no Seller found found for the id=${cerBundleDTO.sellerId}");
        }
        val issuer = traderRepository.findByTraderIdAndTraderRole(cerBundleDTO.issuerId, TraderRole.ISSUER)
        if (ObjectUtils.isEmpty(issuer)) {
            throw ResourceNotFoundException("Could not create CertificateBundle, There is no Issuer found found for the id=${cerBundleDTO.issuerId}");
        }
        val certificateBundle = CertificateBundleMapper().toModel(cerBundleDTO)
        val updatedCerBundle = certificateBundle.copy(seller = seller.get(), issuer = issuer.get())
        val savedCerBundle = certificateBundleRepository.save(updatedCerBundle)
        if (ObjectUtils.isEmpty(savedCerBundle)) {
            throw EntityNotSavedException("The CertificateBundle entity is not saved");
        }
        return CertificateBundleMapper().fromModel(savedCerBundle)

    }

    /**
     * Finds the CertificateBundle by Id and returns the [CertificateBundleDTO]
     */
    fun findCerBundleById(cerBundleId: Long): CertificateBundleDTO {
        val cerBundle = certificateBundleRepository.findById(cerBundleId)
        if (ObjectUtils.isEmpty(cerBundle)) {
            throw ResourceNotFoundException("The resource CertificateBundle not found for id=$cerBundleId");
        }
        return CertificateBundleMapper().fromModel(cerBundle.get());
    }

    /**
     * Finds all the the CertificateBundles and returns the collection of [CertificateBundleDTO]
     */
    fun getAllCerBundlesBySeller(sellerId: Long): Collection<CertificateBundleDTO> {
        val seller = traderRepository.findByTraderIdAndTraderRole(sellerId, TraderRole.SELLER)
        if (ObjectUtils.isEmpty(seller)) {
            throw ResourceNotFoundException("There is no Issuer found found for the id=$sellerId");
        }
        val cerBundleList: List<CertificateBundle> =
            certificateBundleRepository.findBySeller(seller.get());
        if (ObjectUtils.isEmpty(cerBundleList)) {
            throw ResourceNotFoundException("There is no resource CertificateBundle find for the seller id=$sellerId");
        }
        return cerBundleList.map { cerBundle -> CertificateBundleMapper().fromModel(cerBundle) }
    }

}