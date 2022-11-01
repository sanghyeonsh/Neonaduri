package neonaduri.domain.statistics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "visited")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Visited {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visited_id")
    private Long visitedId;

    @Column(name = "visited_region", nullable = false)
    private String visitedRegion;

    @Column(name = "visited_year", nullable = false)
    private Integer visitedYear;

    @Column(name = "visited_num", nullable = false)
    private Integer visitedNum;
}