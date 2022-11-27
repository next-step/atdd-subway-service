package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    protected Favorite() {}

    public Favorite(Member member, Station source, Station target) {
        validate(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validate(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발지와 도착지가 동일할 수 없습니다.");
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
