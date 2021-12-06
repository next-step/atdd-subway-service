package nextstep.subway.favorites.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorites extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member owner;

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    protected Favorites() {
    }

    public Favorites(Member owner, Station upStation, Station downStation) {
        this.owner = owner;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Favorites(Long id, Member owner, Station upStation, Station downStation) {
        this(owner, upStation, downStation);
        this.id = id;
    }

    public boolean isOwner(Long id) {
        return id.equals(owner.getId());
    }

    public Long getId() {
        return id;
    }

    public Member getOwner() {
        return owner;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
