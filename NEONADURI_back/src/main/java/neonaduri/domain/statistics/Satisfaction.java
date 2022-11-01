package neonaduri.domain.statistics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neonaduri.domain.type.SatisfactionType;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "sat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Satisfaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sat_id")
    private Long satId;

    @Column(name = "sat_year", nullable = false)
    private Integer satYear;

    @Column(name = "sat_region", nullable = false)
    private String satRegion;

    @Column(name = "sat_score", nullable = false)
    private Float satScore;

    @Column(name = "sat_type", nullable = false)
    private String satType;
}