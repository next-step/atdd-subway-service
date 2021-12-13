package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target")
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {

    }

    private Favorite(Station source, Station target, Member member) {
        validateCorrectSourceAndTarget(source, target);
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station source, Station target, Member member) {
        Favorite favorite = new Favorite(source, target, member);
        return favorite;
    }

    private void validateCorrectSourceAndTarget(Station source, Station target) {
        if(source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
