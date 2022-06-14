package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.OverFareCalculator.calculateDiscountedFareByAge;
import static nextstep.subway.path.domain.OverFareCalculator.calculateOverFareByDistance;
import static nextstep.subway.path.domain.OverFareCalculator.calculateOverFareByLine;

import java.util.List;
import java.util.Set;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

/**
 * 경로 검색 결과를 리턴하는 도메인 객체 (도메인에서 DTO를 의존하는 상황을 피하기 위함)
 */
public class PathFindResult {
    private List<Station> stations;

    private Set<Line> lines;

    private int distance;

    private SubwayFare fare;

    protected PathFindResult() {

    }

    public PathFindResult(List<Station> stations, Set<Line> lines, int distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
        this.fare = SubwayFare.DEFAULT_FARE;
    }

    public void applyFarePolicy(LoginMember loginMember) {
        int overFareByDistance = calculateOverFareByDistance(this);
        int overFareByLine = calculateOverFareByLine(this);
        int middleSum = this.fare.getValue() + overFareByDistance + overFareByLine;
        if (loginMember.getId() == null) {
            this.fare = SubwayFare.of(middleSum);
            return;
        }
        int discountedFare = calculateDiscountedFareByAge(SubwayFare.of(middleSum), loginMember.getAge());
        this.fare = SubwayFare.of(discountedFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public SubwayFare getFare() {
        return fare;
    }

    public Set<Line> getLines() {
        return lines;
    }

}
