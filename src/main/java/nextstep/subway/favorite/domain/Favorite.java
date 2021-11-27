package nextstep.subway.favorite.domain;

import io.jsonwebtoken.lang.Assert;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_source_station"), nullable = false)
    private Station source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_target_station"), nullable = false)
    private Station target;

    @Column
    private long memberId;

    protected Favorite() {
    }

    private Favorite(Station source, Station target, long memberId) {
        validateStations(source, target);
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public static Favorite from(Station source, Station target, long memberId) {
        return new Favorite(source, target, memberId);
    }

    public Long id() {
        return id;
    }

    public Station source() {
        return source;
    }

    public Station target() {
        return target;
    }

    public long memberId() {
        return memberId;
    }

    private void validateStations(Station source, Station target) {
        Assert.notNull(source, "출발역은 필수입니다.");
        Assert.notNull(target, "도착역은 필수입니다.");
        if (source.equals(target)) {
            throw new IllegalArgumentException(
                String.format("출발역(%s)과 도착역(%s)은 달라야 합니다.", source, target));
        }
    }

}
