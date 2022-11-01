package neonaduri.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId; //따로 지역코드가 있는것으로 알고있어 auto_increment 안함

    @Column(name = "sido", nullable = false)
    private String sido;

    @Column(name = "sigungu", nullable = false)
    private String sigungu;

    @Column(name = "myeon", nullable = false)
    private String myeon;
}
