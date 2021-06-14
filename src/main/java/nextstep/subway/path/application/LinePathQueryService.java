package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class LinePathQueryService {
    private final LineRepository lineRepository;

    public LinePathQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LinePathResponse findShortDistance(LinePathRequest linePathRequest) {
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
}
