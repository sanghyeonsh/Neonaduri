package neonaduri.domain.statistics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "selected")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Selected {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sel_id")
    private Long selId;

    @Column(name = "sel_reason", nullable = false)
    private String selReason;

    @Column(name = "sel_year", nullable = false)
    private Integer visitedYear;

    @Column(name = "sel_percent", nullable = false)
    private Float selPercent;
}
