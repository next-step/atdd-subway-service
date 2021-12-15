package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.path.application.exception.InvalidPathException.NOT_EXIST_STATION;

public class Path {
    private final List<SectionEdge> edges;
    private final List<Station> stations;
    private final int distance;
    private Fare fare;

    public Path(List<SectionEdge> edges, List<Station> stations, int distance) {
        this.edges = edges;
        this.stations = stations;
        this.distance = distance;
    }

    public void calculateFare(LoginMember member, List<Line> lines) {
        this.fare = new Fare();
        this.fare = fare.extraFare(distance, getLineFare(lines));
        if (member.isRequired()) {
            this.fare = fare.discount(member.getAge());
        }
    }

    private int getLineFare(List<Line> lines) {
        List<Line> extraFareLines = lineOnPath(lines);

        return extraFareLines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    private List<Line> lineOnPath(List<Line> lines) {
        return edges.stream()
                .map(edge -> findBy(lines, edge))
                .collect(Collectors.toList());
    }

    private Line findBy(List<Line> lines, SectionEdge edge) {
        Station source = (Station) edge.getSource();
        Station target = (Station) edge.getTarget();

        return lines.stream()
                .filter(line -> line.matchSection(source, target))
                .findFirst()
                .orElseThrow(() -> NOT_EXIST_STATION);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
