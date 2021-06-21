package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    public Favorite() { }

    public Favorite(Station sourceStation, Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Favorite(Long id, Station sourceStation, Station targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
