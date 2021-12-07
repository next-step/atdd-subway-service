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
    private Station departureStation;

    @ManyToOne
    private Station arrivalStation;

    protected Favorites() {
    }

    public Favorites(Member owner, Station departureStation, Station arrivalStation) {
        this.owner = owner;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public Favorites(Long id, Member owner, Station departureStation, Station arrivalStation) {
        this(owner, departureStation, arrivalStation);
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

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }
}
