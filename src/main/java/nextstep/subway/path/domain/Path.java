package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Path {
    private List<Station> stations;
    private Distance distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
    }

    public static Path of(List<Station> vertexList, int weight) {
        return new Path(vertexList, weight);
    }

    public int getLineExtraFare(List<Section> sections) {
        Set<Integer> maxExtraSet = new HashSet<>();

        for(int i = 0; i < stations.size() - 1; i++) {
            int index = i;
            maxExtraSet.add(Sections.findAllSurcharges(sections, stations.get(index), stations.get(index + 1)));
        }

        return maxExtraSet.stream()
                        .mapToInt(value -> value)
                        .max()
                        .orElse(0);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
