package uk.ac.ncl.c8099.wei.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.c8099.wei.backend.dao.entity.RelatedParty;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.util.List;

/**
 * @author wei tan
 */


@Repository
public interface RelatedPartyRepository extends JpaRepository<RelatedParty, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE RelatedParty u SET u.status = ?2,u.result = ?3 WHERE u.id = ?1")
    int updateStateAndCalculatingResultById(Integer id, VerificationStatusEnum status, String result);

    int countByVerificationIdAndStatus(Integer verificationId, VerificationStatusEnum status);

    RelatedParty findByVerificationIdAndPartyAddress(Integer verificationId, String partyAddress);

    List<RelatedParty> findByPartyAddress(String partyAddress);

    List<RelatedParty> findByVerificationId(Integer verificationId);
}
