package nextstep.subway.favorite.domain;

import lombok.Getter;
import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.exception.InvalidAddFavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        validation(sourceStation, targetStation);
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public boolean isEqualId(Long favoriteId) {
        return this.id.equals(favoriteId);
    }

    private void validation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new InvalidAddFavoriteException("출발역과 도착역이 같아 즐겨찾기에 등록할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
