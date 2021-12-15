package nextstep.subway.domain.favorite.domain;

import nextstep.subway.domain.auth.application.AuthorizationException;
import nextstep.subway.domain.member.domain.Member;
import nextstep.subway.domain.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "source_station_id")
    private Station source;

    @OneToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void checkOwner(Member member) {
        if (!this.member.equals(member)) {
            throw new AuthorizationException();
        }
    }
}
