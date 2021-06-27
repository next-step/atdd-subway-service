package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.favorite.NotFoundAnyThingException;
import nextstep.subway.exception.favorite.SameStationException;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    private Long memberId;

    public Favorite() {}

    public Favorite(Long memberId, Station source, Station target) {
        validation(memberId, source, target);
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Long getMemberId() {
        return memberId;
    }

    private void validation(Long memberId, Station source, Station target) {
        if (Objects.isNull(memberId) || Objects.isNull(source) || Objects.isNull(target)) {
            throw new NotFoundAnyThingException();
        }

        if (source.equals(target)) {
            throw new SameStationException();
        }
    }

    public Long getId() {
        return id;
    }

    public static Favorite of(Long memberId, Station source, Station target) {
        return new Favorite(memberId, source, target);
    }

}
