package uk.ac.ncl.c8099.wei.backend.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author wei tan
 */

@Data
@Entity
@Table(name = "Remark")
@EntityListeners(value = AuditingEntityListener.class)
public class Remark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fromAddress;
    private String toAddress;
    private String remark;

}
