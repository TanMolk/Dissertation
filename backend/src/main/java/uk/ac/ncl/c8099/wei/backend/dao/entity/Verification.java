package uk.ac.ncl.c8099.wei.backend.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wei tan
 */

@Data
@Entity
@Table(name = "Verification")
@EntityListeners(value = AuditingEntityListener.class)
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountAddress;

    private String name;

    @Enumerated(EnumType.STRING)
    private VerificationTypeEnum type;

    private String hash;

    private Long certificationNumber;


    @Enumerated(EnumType.STRING)
    private VerificationStatusEnum status;

    @CreatedDate
    private LocalDateTime createTime;

    private Integer blockNumber;

    private String txHash;

    @JoinColumn(name = "verificationId")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RelatedParty> relatedParties;


}
