package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

/**
 * 경로 검색 결과를 리턴하는 도메인 객체 (도메인에서 DTO를 의존하는 상황을 피하기 위함)
 */
public class PathFindResult {
    private static final int FIRST_OVER_FARE_RANGE_START = 10;
    private static final int FIRST_OVER_FARE_RANGE_END = 50;
    private static final int FIRST_OVER_FARE_RANGE_INTERVAL = 5;
    private static final int FIRST_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int SECOND_OVER_FARE_RANGE_START = 50;
    private static final int SECOND_OVER_FARE_RANGE_INTERVAL = 8;
    private static final int SECOND_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int NO_OVER_FARE = 0;

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
        int overFareByDistance = calculateOverFareByDistance();
        int overFareByLine = calculateOverFareByLine();
        int middleSum = this.fare.getValue() + overFareByDistance + overFareByLine;
        if (loginMember.getId() == null) {
            this.fare = SubwayFare.of(middleSum);
            return;
        }
        this.fare = loginMember.discountFareByAge(SubwayFare.of(middleSum));
    }

    public int calculateOverFareByDistance() {
        return calculateOverFareInFirstRange()
                + calculateOverFareInSecondRange();
    }

    private int calculateOverFareInFirstRange() {
        int overDistance = Math.min(distance, FIRST_OVER_FARE_RANGE_END) - FIRST_OVER_FARE_RANGE_START;
        if (!hasOverDistance(overDistance)) {
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / FIRST_OVER_FARE_RANGE_INTERVAL) + 1)
                * FIRST_OVER_FARE_RANGE_FARE_UNIT);
    }

    private boolean hasOverDistance(int overDistance) {
        return overDistance > 0;
    }

    private int calculateOverFareInSecondRange() {
        int overDistance = distance - SECOND_OVER_FARE_RANGE_START;
        if (!hasOverDistance(overDistance)) {
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_FARE_RANGE_INTERVAL) + 1)
                * SECOND_OVER_FARE_RANGE_FARE_UNIT);
    }

    public int calculateOverFareByLine() {
        return lines.stream()
                .mapToInt(Line::getExtraCharge)
                .max()
                .getAsInt();
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
