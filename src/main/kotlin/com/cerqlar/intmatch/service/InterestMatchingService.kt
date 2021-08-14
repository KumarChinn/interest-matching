package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.dto.match.IntMatchingRequest
import com.cerqlar.intmatch.dto.match.IntMatchingResponse
import com.cerqlar.intmatch.model.bundle.CertificateBundle
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.repository.CertificateBundleRepository
import com.cerqlar.intmatch.repository.InterestRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.NoMatchFoundException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.CertificateBundleMapper
import com.cerqlar.intmatch.utils.mapper.InterestMapper
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.util.ArrayList


/**
 * Created by chinnku on Aug, 2021
 * Service class for matching the resources with certificate bundles
 */
@Service
class InterestMatchingService(
    private val interestRepository: InterestRepository,
    private val traderRepository: TraderRepository,
    private val certificateBundleRepository: CertificateBundleRepository
) {

    /**
     * Finds the matching certificate bundles for the given interest id
     * and returns the [IntMatchingResponse]
     * This does not assign the certificates to interest
     *
     * matching logic:
     * find a exact match OR
     * Find a single match which can fulfill the interest OR
     * Find the least number of CertificateBundles to fulfill the interest
     */
    fun findMatchingCerBundles(intMatchingRequest: IntMatchingRequest): IntMatchingResponse {
        return assignMatchingCerBundles(intMatchingRequest, "find")
    }

    /**
     * Finds the matching certificate bundles for the given interest id
     * If matching found - interest is closed and the certificates in the certificate bundles are consumed
     * and returns the [IntMatchingResponse]
     *
     * matching logic:
     * find a exact match using modified Subset Sum Algorithm OR
     * Find a single match which can fulfill the interest OR
     * Find the least number of CertificateBundles to fulfill the interest
     *
     * Result:
     * Interest status set to CLOSED
     * CertificateBundle updated with consumedQty
     */
    fun assignMatchingCerBundles(intMatchingRequest: IntMatchingRequest): IntMatchingResponse {
        return assignMatchingCerBundles(intMatchingRequest, "post")
    }

    private fun assignMatchingCerBundles(
        intMatchingRequest: IntMatchingRequest,
        requestType: String
    ): IntMatchingResponse {

        //find seller
        val seller = traderRepository.findByTraderIdAndTraderRole(intMatchingRequest.sellerId, TraderRole.SELLER)
        if (ObjectUtils.isEmpty(seller)) {
            throw ResourceNotFoundException("Could not create CertificateBundle, There is no Seller found found for the id=${intMatchingRequest.sellerId}")
        }

        //find interest
        val intOptional = interestRepository.findByintIdAndStatus(intMatchingRequest.intId, InterestStatus.OPEN)
        if (ObjectUtils.isEmpty(intOptional)) {
            throw ResourceNotFoundException("The resource Interest not found for id=${intMatchingRequest.intId} and status=OPEN")
        }
        val interest = intOptional.get()

        //find cer-bundles
        val cerBundleList = certificateBundleRepository.findBySellerAndEnergySource(seller.get(), interest.energySource)
        if (ObjectUtils.isEmpty(cerBundleList)) {
            throw NoMatchFoundException("There is no CertificateBundle found for the seller id${intMatchingRequest.sellerId} and source${interest.energySource}")
        }

        val responseList = mutableListOf<CertificateBundle>()

        //Find Exact match
        val matchedList = arrayListOf<CertificateBundle>()
        findSubsetOfListSorSum(cerBundleList, interest.qty, matchedList)

        if (!ObjectUtils.isEmpty(matchedList)) {
            var intQty = interest.qty
            matchedList.forEach {
                if ("post".equals(requestType)) {
                    if (availableQty(it) <= intQty) {
                        val updatedCerBundle =
                            it.copy(assignedInts = mutableListOf(interest), consumedQty = it.consumedQty + it.qty)
                        responseList.add(updatedCerBundle)
                    }
                    intQty -= it.qty
                } else {
                    responseList.add(it)
                }
            }
        } else {
            //Find a single match which satisfies
            val matchingCerBundle = cerBundleList.firstOrNull { availableQty(it) > interest.qty }
            if (matchingCerBundle != null) {
                if ("post".equals(requestType)) {
                    val updatedCerBundle = matchingCerBundle.copy(
                        assignedInts = mutableListOf(interest),
                        consumedQty = matchingCerBundle.consumedQty + interest.qty
                    )
                    responseList.add(updatedCerBundle)
                } else {
                    responseList.add(matchingCerBundle)
                }
            } else {
                //Find the least number of CertificateBundles to fulfill the interest
                var intQty = interest.qty
                cerBundleList.filter { availableQty(it) > 0 }.forEach {
                    if (intQty > 0) {
                        if ("post".equals(requestType)) {
                            val qtyToConsume: Long
                            if (availableQty(it) > intQty) {
                                qtyToConsume = intQty
                            } else {
                                qtyToConsume = it.qty
                            }
                            val updatedCerBundle = it.copy(
                                assignedInts = mutableListOf(interest),
                                consumedQty = it.consumedQty + qtyToConsume
                            )
                            responseList.add(updatedCerBundle)
                        } else {
                            responseList.add(it)
                        }
                    }
                    intQty -= it.qty
                }
                if (intQty > 0) {
                    responseList.clear()
                }
            }
        }

        if (ObjectUtils.isEmpty(responseList)) {
            throw NoMatchFoundException("The available CertificateBundles from the Seller could not fulfill the Interest at the moment")
        }

        //consume the qty from CerBundle
        if ("post".equals(requestType)) {
            responseList.forEach {
                certificateBundleRepository.save(it)
            }
            //close interest status
            closeInterest(interest)
        }
        return IntMatchingResponse(
            InterestMapper().fromModel(interest),
            responseList.map { cerBundle -> CertificateBundleMapper().fromModel(cerBundle) })
    }

    /**
     * modified Subset Sum algorithm to find the matching list of items for the given sum
     * This returns only one matching combination, not all combinations returned
     *
     */
    fun findSubsetOfListSorSum(
        inList: List<CertificateBundle>,
        inMatchSum: Long,
        matchedList: ArrayList<CertificateBundle>
    ): Boolean {
        // if inMatchSum=0, we have found a solution
        if (inMatchSum == 0L) {
            return true
        }
        // Sum < 0. No point going forward in the path
        if (inMatchSum < 0) return false
        // Reached at the end of a path and this path is not the solution
        if (inList.size == 0 && inMatchSum != 0L) return false
        val newList = ArrayList(inList)
        newList.removeAt(0)
        // Select the first element
        matchedList.add(inList[0])
        if (findSubsetOfListSorSum(newList, inMatchSum - availableQty(inList[0]), matchedList)) {
            return true
        }
        // Reject the first element
        matchedList.removeAt(matchedList.size - 1)
        return findSubsetOfListSorSum(newList, inMatchSum, matchedList)
    }

    /**
     * Calculate availableQty and return [Long]
     */
    private fun availableQty(cerBundle: CertificateBundle): Long {
        return cerBundle.qty - cerBundle.consumedQty
    }

    /**
     * Close the Interest
     */
    private fun closeInterest(interest: Interest) {
        val updatedInt = interest.copy(status = InterestStatus.CLOSED)
        interestRepository.save(updatedInt)
    }
}