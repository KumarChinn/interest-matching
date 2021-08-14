package com.cerqlar.intmatch.service

import com.cerqlar.intmatch.model.interest.Interest
import com.cerqlar.intmatch.model.interest.InterestDTO
import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.common.TraderRole
import com.cerqlar.intmatch.repository.InterestRepository
import com.cerqlar.intmatch.repository.TraderRepository
import com.cerqlar.intmatch.utils.exception.EntityNotSavedException
import com.cerqlar.intmatch.utils.exception.ResourceNotFoundException
import com.cerqlar.intmatch.utils.mapper.InterestMapper
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

/**
 * Created by chinnku on Aug, 2021
 * Service class for the Interest resource
 */
@Service
class InterestService(
    private val interestRepository: InterestRepository,
    private val traderRepository: TraderRepository
) {
    /**
     * Creates the Interest and returns the [InterestDTO]
     */
    fun createNewInterest(interestDTO: InterestDTO): InterestDTO {
        val trader = traderRepository.findByTraderIdAndTraderRole(interestDTO.intBuyerId, TraderRole.BUYER)
        if (ObjectUtils.isEmpty(trader)) {
            throw ResourceNotFoundException("Could not create Interest, There is no Buyer found found for the id=" + interestDTO.intBuyerId);
        }
        val interest = InterestMapper().toModel(interestDTO)
        val updatedInt = interest.copy(status = InterestStatus.OPEN,intBuyer = trader.get())
        val intSaved = interestRepository.save(updatedInt)
        if (ObjectUtils.isEmpty(intSaved)) {
            throw EntityNotSavedException("The Interest entity is not saved");
        }
        return InterestMapper().fromModel(intSaved)

    }

    /**
     * Finds the Interest resource by Id and returns the [InterestDTO]
     */
    fun findInterestById(interestId: Long): InterestDTO {
        val interest = interestRepository.findById(interestId)
        if (ObjectUtils.isEmpty(interest)) {
            throw ResourceNotFoundException("The resource Interest not found for id=$interestId");
        }
        return InterestMapper().fromModel(interest.get());
    }

    /**
     * Finds all the Interest resource and returns the collection of [InterestDTO]
     */
    fun getAllInterests(): Collection<InterestDTO> {
        val cerBundleList: List<Interest> = interestRepository.findAll();
        if (ObjectUtils.isEmpty(cerBundleList)) {
            throw ResourceNotFoundException("There no resource Interest found");
        }
        return cerBundleList.map { interest -> InterestMapper().fromModel(interest) }
    }

}