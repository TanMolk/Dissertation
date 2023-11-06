package uk.ac.ncl.c8099.wei.backend.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.time.LocalDateTime;

/**
 * @author wei tan
 */

@Data
@Entity
@Table(name = "RelatedParty")
@EntityListeners(value = AuditingEntityListener.class)
public class RelatedParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer verificationId;
    private String partyAddress;

    @Enumerated(EnumType.STRING)
    private VerificationStatusEnum status;
    private String result;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;


}
