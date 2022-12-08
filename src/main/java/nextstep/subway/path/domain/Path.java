package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private final List<SectionEdgeWeight> sectionEdgeWeights;
    private final List<Station> stations;
    private final Distance distance;

    public Path(List<SectionEdgeWeight> sectionEdgeWeights, List<Station> stations, Distance distance) {
        this.sectionEdgeWeights = sectionEdgeWeights;
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Sections getSections() {
        return new Sections(
                sectionEdgeWeights.stream()
                    .map(SectionEdgeWeight::getSection)
                    .collect(Collectors.toList()));
    }
}
