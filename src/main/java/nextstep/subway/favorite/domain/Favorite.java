package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        requireNonNull(member, "member");
        requireNonNull(sourceStation, "sourceStation");
        requireNonNull(targetStation, "targetStation");
        validateSameStation(sourceStation, targetStation);
        this.member = member;
        this.source = sourceStation;
        this.target = targetStation;
    }

    protected Favorite() {
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new CannotCreatingFavoriteException("출발지와 목적지가 같을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
