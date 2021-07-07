package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station sourceStation = stationService.findById(pathRequest.getSource());
        Station targetStation = stationService.findById(pathRequest.getTarget());
        List<Section> allSections = lineRepository.findAll().stream()
            .flatMap(line -> line.getSections().stream())
            .collect(toList());

        PathFinder pathFinder = PathFinder.of(allSections);
        GraphPath path = pathFinder.findShortestPath(sourceStation, targetStation);
        List<Station> stations = path.getVertexList();

        return PathResponse.of(stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList()), path.getWeight());
    }
}
