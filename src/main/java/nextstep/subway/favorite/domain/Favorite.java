package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    protected Favorite() {
    }

    private Favorite(Station source, Station target, Member owner) {
        this.source = source;
        this.target = target;
        this.owner = owner;
    }

    public static Favorite of(Station source, Station target, Member owner) {
        return new Favorite(source, target, owner);
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
