package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    public static final String TARGET_SOURCE_SAME_EXCEPTION_MESSAGE = "출발역과 도착역이 같을 수 없다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    public Favorite(Member member, Station source, Station target) {
        validateStation(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validateStation(Station target, Station source) {
        if (target.equals(source)) {
            throw new IllegalArgumentException(TARGET_SOURCE_SAME_EXCEPTION_MESSAGE);
        }
    }

    public Long getId() {
        return this.id;
    }

    public Station getSource() {
        return this.source;
    }

    public Station getTarget() {
        return this.target;
    }
}
