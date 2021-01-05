package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathFinderResponse findShortestPath(final long departureId, final long arrivalId) {
        final Station departureStation = stationRepository.findById(departureId).orElseThrow(NotFoundException::new);
        final Station arrivalStation = stationRepository.findById(arrivalId).orElseThrow(NotFoundException::new);
        final List<Line> allLines = lineRepository.findAll();

        final PathFinder pathFinder = PathFinder.of(allLines);

        final GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(departureStation, arrivalStation);
        return PathFinderResponse.of(shortestPath);
    }
}
