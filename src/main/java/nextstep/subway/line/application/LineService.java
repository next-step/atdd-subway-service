package nextstep.subway.line.application;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());
        Line line = lineRepository.save(request.toLine(upStation, downStation));

        return LineResponse.of(line);
    }

    public List<LineResponse> findLines() {
        List<Line> line = lineRepository.findAll();
        return line.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = findLineById(id);
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = findLineById(id);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long id, SectionRequest request) {
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());
        Line line = findLineById(id);

        line.addSection(request.toSection(upStation, downStation));
        lineRepository.save(line);
    }

    @Transactional
    public void removeLineStation(Long id, Long stationId) {
        Line line = findLineById(id);
        Station station = findStation(stationId);
        line.deleteSection(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.ENTITY_NOT_FOUND_BY_ID.getMessage()));
    }

    private Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.ENTITY_NOT_FOUND_BY_ID.getMessage()));
    }
}
