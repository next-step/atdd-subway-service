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
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station sourceStation = stationRepository.findById(pathRequest.getSource())
            .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(pathRequest.getTarget())
            .orElseThrow(NoSuchElementException::new);
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
