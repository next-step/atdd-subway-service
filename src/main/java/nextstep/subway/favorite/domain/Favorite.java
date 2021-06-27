package nextstep.subway.favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    public Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member", unique = true)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target")
    private Station target;

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
