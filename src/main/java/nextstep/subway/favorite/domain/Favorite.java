package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    public static final String TARGET_SOURCE_SAME_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없다.";
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    public Favorite(Member member, Station target, Station source) {
        validateStation(target, source);
        this.member = member;
        this.target = target;
        this.source = source;
    }

    private void validateStation(Station target, Station source) {
        if (target.equals(source)) {
            throw new IllegalArgumentException(TARGET_SOURCE_SAME_EXCEPTION_MESSAGE);
        }
    }
}
