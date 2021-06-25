package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "memberId")
    Member member;

    @OneToOne
    @JoinColumn(name = "sourceStationId")
    private Station source;

    @OneToOne
    @JoinColumn(name = "targetStationId")
    private Station target;

    public Favorite() {

    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(null, member, source, target);
    }

    private Favorite(Long id, Member member, Station source, Station target) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
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
