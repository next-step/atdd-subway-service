package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_station_id")
    private Station departureStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {
    }

    public Favorite(Station departureStation, Station arrivalStation, Member member) {
        validate(departureStation, arrivalStation, member);
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.member = member;
    }

    private void validate(Station departureStation, Station arrivalStation, Member member) {
        if (departureStation == null || arrivalStation == null || member == null) {
            throw new IllegalArgumentException("필수값 누락입니다.");
        }
    }

    public void changeMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

