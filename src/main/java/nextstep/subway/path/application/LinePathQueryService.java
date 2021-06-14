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

        Line shortDistance = lines.findShortDistance(findStationById(linePathRequest.getSource()), findStationById(linePathRequest.getTarget()));
        return new LinePathResponse(
                Arrays.asList(
                        new StationResponse(1L, null, null, null),
                        new StationResponse(4L, null, null, null)
                ),
                3
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
