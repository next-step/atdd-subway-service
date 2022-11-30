package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    private static final String ERROR_MESSAGE_FAVORITE_SAME_STATIONS = "출발역과 도착역이 같을 수 없습니다.";
    private static final String ERROR_MESSAGE_FAVORITE_NOT_NULL_STATIONS = "필수값을 확인해주세요. (회원정보, 출발역, 도착역)";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "departure_id", nullable = false)
    private Station departureStation;
    @ManyToOne
    @JoinColumn(name = "arrival_id", nullable = false)
    private Station arrivalStation;

    protected Favorite() {}

    private Favorite(Member member, Station departureStation, Station arrivalStation) {
        validFavorite(member, departureStation, arrivalStation);
        member.addFavorite(this);
        this.member = member;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    public static Favorite of(Member member, Station departureStation, Station arrivalStation) {
        return new Favorite(member, departureStation, arrivalStation);
    }

    private void validFavorite(Member member, Station departureStation, Station arrivalStation) {
        validNotNull(member, departureStation, arrivalStation);
        validSameStations(departureStation, arrivalStation);
    }

    private void validNotNull(Member member, Station departureStation, Station arrivalStation) {
        if (Objects.isNull(member) || Objects.isNull(departureStation) || Objects.isNull(arrivalStation)) {
            throw new InvalidParameterException(ERROR_MESSAGE_FAVORITE_NOT_NULL_STATIONS);
        }
    }

    private void validSameStations(Station departureStation, Station arrivalStation) {
        if (departureStation.isSameStation(arrivalStation)) {
            throw new InvalidParameterException(ERROR_MESSAGE_FAVORITE_SAME_STATIONS);
        }
    }

    public Long getId() {
        return id;
    }

    public Station departureStation() {
        return departureStation;
    }

    public Station arrivalStation() {
        return arrivalStation;
    }
}
