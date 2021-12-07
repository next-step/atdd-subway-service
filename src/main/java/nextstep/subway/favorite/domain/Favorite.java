package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.station.domain.Station;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_source_station"))
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_target_station"))
    private Station target;

    protected Favorite() {
    }

    private Favorite(Long memberId, Station source, Station target) {
        validateParam(memberId, source, target);

        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Long memberId, Station source, Station target) {
        return new Favorite(memberId, source, target);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    private void validateParam(Long memberId, Station source, Station target) {
        if (memberId == null || memberId <= 0) {
            throw new FavoriteException("맴버 ID는 비어있거나 음수 또는 0일 수 없습니다.");
        }

        if (ObjectUtils.isEmpty(source)) {
            throw new FavoriteException("Source Station은 비어 있을 수 없습니다.");
        }

        if (ObjectUtils.isEmpty(target)) {
            throw new FavoriteException("Target Station은 비어 있을 수 없습니다.");
        }
    }
}
