package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite() {}

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public List<Station> findAllStations() {
        return Arrays.asList(sourceStation, targetStation);
    }

    public Long getId() {
        return id;
    }
}
