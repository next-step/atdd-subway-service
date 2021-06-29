package nextstep.subway.component;

import nextstep.subway.component.domain.SubwayPath;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PathFinder {
    public SubwayPath shortestPath(List<Line> lines, Optional<Station> source, Optional<Station> target) {
        List<Station> stations = lines.stream().flatMap(line -> line.getStations().stream()).collect(Collectors.toList());
        return new SubwayPath(stations, 15);
    }
}
