package com.cerqlar.intmatch.utils.mapper

import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.bundle.CertificateBundleDTO

/**
 * Created by chinnku on Aug, 2021
 * Mapper to convert from DTO to model and model to DTO
 * CertificateBundleMapper
 */
class CertificateBundleMapper : Mapper<CertificateBundleDTO, CertificateBundle> {
    override fun fromModel(model: CertificateBundle): CertificateBundleDTO = CertificateBundleDTO(
        model.cerBundleId,
        model.seller!!.traderId,
        model.issuer!!.traderId,
        model.energySource,
        (model.qty - model.consumedQty),
        model.qty,
        model.consumedQty,
        model.issuedDate
    )

    override fun toModel(domain: CertificateBundleDTO): CertificateBundle = CertificateBundle(
        domain.cerBundleId,
        null,
        null,
        domain.energySource,
        domain.qty,
        domain.consumedQty,
        domain.issuedDate
    )
}