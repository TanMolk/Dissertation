package uk.ac.ncl.c8099.wei.backend.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 * @author wei tan
 */

@Data
@Entity
@Table(name = "Account")
@EntityListeners(value = AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;

    @JoinColumn(referencedColumnName = "address", name = "accountAddress")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Verification> verifications;

    @JoinColumn(referencedColumnName = "address", name = "fromAddress")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Remark> remarks;

}
