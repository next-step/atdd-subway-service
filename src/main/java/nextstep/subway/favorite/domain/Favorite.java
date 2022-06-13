package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    Station sourceStation;


    @ManyToOne
    @JoinColumn(name = "target_station_id")
    Station targetStation;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return this.id;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public Station getSourceStation() {
        return sourceStation;
    }
}
