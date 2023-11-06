package uk.ac.ncl.c8099.wei.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Remark;

import java.util.List;

/**
 * @author wei tan
 */


@Repository
public interface RemarkRepository extends JpaRepository<Remark, Integer> {

    List<Remark> findRemarkByFromAddress(String fromAddress);

    int countByFromAddressAndToAddress(String fromAddress, String toAddress);

    int countByFromAddressAndRemark(String fromAddress, String remark);
}
