package nextstep.subway.favorite.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Favorite extends BaseEntity {

    private static final String SAME_SOURCE_TARGET = "출발과 도착이 같아 즐겨찾기로 추가할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        validate(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
        member.addFavorite(this);
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new FavoriteException(SAME_SOURCE_TARGET);
        }
    }

    public boolean isEqualToId(Long compareId) {
        return this.id.equals(compareId);
    }
}
