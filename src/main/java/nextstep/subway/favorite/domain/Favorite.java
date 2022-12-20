package nextstep.subway.favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(name = "source_id", nullable = false)
    private Long sourceStationId;

    @Column(name = "target_id", nullable = false)
    private Long targetStationId;

    public Favorite() {
    }

    public Long getId() {
        return id;
    }
}
