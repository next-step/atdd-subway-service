package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.Route;
import nextstep.subway.path.utils.SectionEdge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestRoute(long sourceId, long targetId) {
        final Station startStation = stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        final Station endStation = stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);
        GraphPath<Station, SectionEdge> graphPath = new Route().getShortestRoute(lineRepository.findAll(), startStation, endStation);
        return PathResponse.of(graphPath);
    }

}
