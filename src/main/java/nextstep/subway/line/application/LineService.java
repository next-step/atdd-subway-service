package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.line.LineNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    private Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    public List<LineResponse> findLines() {
        return LineResponse.ofList(lineRepository.findAll());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLine(id));
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line persistLine = findLine(id);
        persistLine.update(request.toLine());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLine(lineId);
        Station upStation = stationService.findStation(request.getUpStationId());
        Station downStation = stationService.findStation(request.getDownStationId());
        line.addSection(request.toSection(line, upStation, downStation));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLine(lineId);
        line.removeStation(stationId);
    }
}
