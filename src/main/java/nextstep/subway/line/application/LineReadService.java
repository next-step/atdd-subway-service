package nextstep.subway.line.application;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.common.exception.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

@Service
@Transactional(readOnly = true)
public class LineReadService {
    private static final String LINE = "노선";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineReadService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.ofList(persistLines);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(LINE));
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    public Station getUpStation(Long upStationId) {
        return stationRepository.findById(upStationId)
            .orElseThrow(() -> new NotFoundException(LINE));
    }

    public Station getDownStation(Long downStationId) {
        return stationRepository.findById(downStationId)
            .orElseThrow(() -> new NotFoundException(LINE));
    }
}
