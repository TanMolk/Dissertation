package uk.ac.ncl.c8099.wei.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Account;

import java.util.List;

/**
 * @author wei tan
 */


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    int countAccountByAddress(String address);

    List<Account> findByAddress(String address);
    @Transactional(rollbackFor = Exception.class)
    int removeAccountByAddress(String address);

}
