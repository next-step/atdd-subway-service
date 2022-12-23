package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_favorite_member"), nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_source_station"), nullable = false)
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_target_station"), nullable = false)
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        validateFavorite(sourceStation, targetStation);
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    private void validateFavorite(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
