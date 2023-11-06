package uk.ac.ncl.c8099.wei.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.util.List;

/**
 * @author wei tan
 */


@Repository
public interface VerificationRepository extends JpaRepository<Verification, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Verification u SET u.status = ?2 WHERE u.id = ?1")
    int updateStateById(Integer id, VerificationStatusEnum status);

    Verification findByCertificationNumber(long certificationNumber);

    List<Verification> findByCertificationNumberIn(List<Long> idList);

}
