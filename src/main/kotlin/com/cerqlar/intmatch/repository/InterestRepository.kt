package com.cerqlar.intmatch.repository

import com.cerqlar.intmatch.model.common.InterestStatus
import com.cerqlar.intmatch.model.interest.Interest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by chinnku on Aug, 2021
 * InterestRepository
 */
@Repository
interface InterestRepository : JpaRepository<Interest, Long> {
    /**
     * Find Interest by id and Status
     */
    fun findByintIdAndStatus(intId: Long, intStatus: InterestStatus): Optional<Interest>
}