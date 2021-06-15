package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class LinePathQueryService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final ShortestDistance shortestDistance;

    public LinePathQueryService(LineRepository lineRepository, StationRepository stationRepository, ShortestDistance shortestDistance) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.shortestDistance = shortestDistance;
    }

    public LinePathResponse findShortDistance(LinePathRequest linePathRequest) {
        Station source = findStationById(linePathRequest.getSource());
        Station target = findStationById(linePathRequest.getTarget());

        Line shortDistance = findAll().findShortestLine(shortestDistance, source, target);

        return new LinePathResponse(
                shortDistance.findShortestRoute(shortestDistance, source, target),
                shortDistance.calcDistanceBetween(shortestDistance, source, target)
        );
    }

    private Lines findAll() {
        return new Lines(lineRepository.findAll());
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
