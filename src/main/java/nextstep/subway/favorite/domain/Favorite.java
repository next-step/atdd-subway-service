package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    private static final String ERROR_MESSAGE_FAVORITE_SAME_STATIONS = "출발역과 도착역이 같을 수 없습니다.";
    private static final String ERROR_MESSAGE_FAVORITE_NOT_NULL_MEMBER = "회원정보를 확인해주세요.";
    private static final String ERROR_MESSAGE_FAVORITE_NOT_NULL_DEPARTURE_STATION = "출발역을 확인해주세요.";
    private static final String ERROR_MESSAGE_FAVORITE_NOT_NULL_ARRIVAL_STATION = "도착역을 확인해주세요.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "departure_id", nullable = false)
    private Station departureStation;
    @ManyToOne
    @JoinColumn(name = "arrival_id", nullable = false)
    private Station arrivalStation;

    protected Favorite() {}

    private Favorite(long memberId, Station departureStation, Station arrivalStation) {
        validFavorite(memberId, departureStation, arrivalStation);
        this.memberId = memberId;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }

    private Favorite(long id, long memberId, Station departureStation, Station arrivalStation) {
        this(memberId, departureStation, arrivalStation);
        this.id = id;
    }

    public static Favorite of(long memberId, Station departureStation, Station arrivalStation) {
        return new Favorite(memberId, departureStation, arrivalStation);
    }

    public static Favorite of(long id, long memberId, Station departureStation, Station arrivalStation) {
        return new Favorite(id, memberId, departureStation, arrivalStation);
    }

    private void validFavorite(long memberId, Station departureStation, Station arrivalStation) {
        isPositive(memberId);
        validNotNull(departureStation, arrivalStation);
        validSameStations(departureStation, arrivalStation);
    }

    private void isPositive(long memberId) {
        if (memberId < 1) {
            throw new InvalidParameterException(ERROR_MESSAGE_FAVORITE_NOT_NULL_MEMBER);
        }
    }

    private void validNotNull(Station departureStation, Station arrivalStation) {
        if (Objects.isNull(departureStation)) {
            throw new InvalidParameterException(ERROR_MESSAGE_FAVORITE_NOT_NULL_DEPARTURE_STATION);
        }

        if (Objects.isNull(arrivalStation)) {
            throw new InvalidParameterException(ERROR_MESSAGE_FAVORITE_NOT_NULL_ARRIVAL_STATION);
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
