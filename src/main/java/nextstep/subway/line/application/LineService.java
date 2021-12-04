package nextstep.subway.line.application;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.common.exception.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

@Service
@Transactional
public class LineService {
    private static final String LINE = "노선";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line newLine = lineRepository.save(Line.of(
            request.getName(),
            request.getColor(),
            getUpStation(request.getUpStationId()),
            getDownStation(request.getDownStationId()),
            Distance.from(request.getDistance()))
        );
        return LineResponse.of(newLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.ofList(persistLines);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(LINE));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        findLineById(id).update(
            Line.from(lineUpdateRequest)
        );
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = getUpStation(request.getUpStationId());
        Station downStation = getDownStation(request.getDownStationId());
        line.addSection(upStation, downStation, Distance.from(request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException(LINE));
        line.removeLineStation(station);
    }

    @Transactional(readOnly = true)
    private Station getUpStation(Long upStationId) {
        return stationRepository.findById(upStationId)
            .orElseThrow(() -> new NotFoundException(LINE));
    }

    @Transactional(readOnly = true)
    private Station getDownStation(Long downStationId) {
        return stationRepository.findById(downStationId)
            .orElseThrow(() -> new NotFoundException(LINE));
    }
}
