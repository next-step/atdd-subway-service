package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.BasicFare;
import nextstep.subway.fare.policy.DistanceSurchargePolicy;
import nextstep.subway.fare.policy.LineSurchargePolicy;
import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.fare.policy.MemberDiscountPolicy;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class Path {
    private static final String ERROR_MESSAGE_STATIONS_LESS_THAN_MIN_SIZE_FORMAT = "노선에는 최소 %d개의 지하철역이 포함되어야 합니다.";
    private static final int MINIMUM_STATION_SIZE = 2;

    private final Stations stations;
    private final Distance distance;
    private final Lines lines;

    private Path(Stations stations, Distance distance, Lines lines) {
        validSize(stations);
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public static Path of(Stations stations, Distance distance) {
        return new Path(stations, distance, Lines.EMPTY_LIST);
    }

    public static Path of(Stations stations, Distance distance, List<SectionEdge> edges) {
        return new Path(stations, distance, toLines(edges));
    }

    private static void validSize(Stations stations) {
        if (stations.isLessThan(MINIMUM_STATION_SIZE)) {
            throw new InvalidParameterException(String.format(ERROR_MESSAGE_STATIONS_LESS_THAN_MIN_SIZE_FORMAT, MINIMUM_STATION_SIZE));
        }
    }

    private static Lines toLines(List<SectionEdge> edges) {
        return Lines.from(edges.stream()
                .map(SectionEdge::getLine)
                .distinct()
                .collect(Collectors.toList()));
    }

    public Fare maxLineSurcharge() {
        return lines.maxLineSurcharge();
    }

    public Fare calculateFare(LoginMember loginMember) {
        FarePolicy farePolicy = new BasicFare();
        farePolicy = DistanceSurchargePolicy.of(farePolicy, distance);
        farePolicy = LineSurchargePolicy.of(farePolicy, this);
        farePolicy = MemberDiscountPolicy.of(farePolicy, loginMember);
        return farePolicy.calculateFare();
    }

    public List<Station> stations() {
        return stations.list();
    }

    public List<Line> lines() {
        return lines.list();
    }

    public int distanceValue() {
        return distance.value();
    }

    public Distance distance() {
        return distance;
    }
}
