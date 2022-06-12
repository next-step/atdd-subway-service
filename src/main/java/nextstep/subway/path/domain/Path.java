package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Set<Line> lines;

    public Path(List<Station> stations, int distance, List<SectionEdge> edges) {
        this.stations = stations;
        this.distance = distance;
        this.lines = toLines(edges);
    }

    private Set<Line> toLines(List<SectionEdge> edges) {
        return edges.stream()
                    .map(SectionEdge::getLine)
                    .collect(Collectors.toSet());
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public Fare calculateFare(LoginMember loginMember) {
        Fare distanceFare = DistanceFarePolicy.calculate(distance);
        Fare additionalFare = applyAdditionalFare();
        Fare totalFare = distanceFare.plus(additionalFare);
        if (loginMember.isLogged()) {
            AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(loginMember.getAge());
            totalFare = ageDiscountPolicy.discount(totalFare);
        }
        return totalFare;
    }

    private Fare applyAdditionalFare() {
        return lines.stream()
                    .map(Line::getAdditionalFare)
                    .max(Fare::compareTo)
                    .orElse(Fare.ZERO);
    }
}
