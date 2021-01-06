package nextstep.subway.favorites.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "departureId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station departure;

    @JoinColumn(name = "arrivalId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Station arrival;

    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Long getId() {
        return id;
    }

    public Station getDeparture() {
        return departure;
    }

    public Station getArrival() {
        return arrival;
    }

    public Member getMember() {
        return member;
    }

    public Favorites() {
    }

    public Favorites(Station departure, Station arrival, Member member) {
        this.departure = departure;
        this.arrival = arrival;
        this.member = member;
    }
}
