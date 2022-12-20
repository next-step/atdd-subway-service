package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    public Favorite() {
    }

    public Long getId() {
        return id;
    }
}
