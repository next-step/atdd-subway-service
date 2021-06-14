package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class LinePathQueryService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LinePathQueryService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LinePathResponse findShortDistance(LinePathRequest linePathRequest) {
        Lines lines = findAll();

        Station source = findStationById(linePathRequest.getSource());
        Station target = findStationById(linePathRequest.getTarget());

        Line shortDistance = lines.findShortDistance(source, target);

        return new LinePathResponse(shortDistance, source, target);
    }

    private Lines findAll() {
        return new Lines(lineRepository.findAll());
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
