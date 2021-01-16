package nextstep.subway.member.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_station_id")
    private Station departureStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    protected Favorite() {}

    public Favorite(Member member, Station departureStation, Station arrivalStation) {
        changeMember(member);
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public void changeMember(Member member) {
        this.member = member;
        member.addFavorite(this);
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void changeDepartureStation(Station station) {
        this.departureStation = station;
    }

    public void changeArrivalStation(Station station) {
        this.arrivalStation = station;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
