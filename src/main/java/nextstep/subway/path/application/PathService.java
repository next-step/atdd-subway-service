package nextstep.subway.path.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.NoDiscountStrategy;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {

        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = new PathFinder(lines);
        ShortestPath shortestPath = pathFinder.findShortestPath(source, target);
        Fare fare = new Fare(shortestPath.getDistance(),
                             getBelongLines(lines, shortestPath),
                             new NoDiscountStrategy());

        return PathResponse.of(shortestPath, fare);
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                    .flatMap(line -> line.getSections().stream())
                    .collect(toList());
    }

    private Set<Line> getBelongLines(List<Line> lines, ShortestPath shortestPath) {

        Set<Line> lineSet = new HashSet<>();
        List<Station> path = shortestPath.getPath();
        List<Section> sections = getAllSections(lines);

        for (int i = 0; i < path.size() - 1; i++) {

            Station station = path.get(i);
            Station nextStation = path.get(i + 1);

            for (Section section : sections) {
                if (section.hasStations(station, nextStation)) {
                    lineSet.add(section.getLine());
                    break;
                }
            }
        }

        return lineSet;
    }
}
