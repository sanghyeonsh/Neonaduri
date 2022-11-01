package neonaduri.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "classification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "md_class", nullable = false)
    private String mdClass;

    @Column(name = "sm_class", nullable = false)
    private String smClass;
}
