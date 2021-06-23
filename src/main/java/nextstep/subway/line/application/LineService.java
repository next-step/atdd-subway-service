package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        Line line = new Line(request.getName(), request.getColor());
        line.addSection(section);
        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine, createStationResponses(persistLine));
    }

    private List<StationResponse> createStationResponses(Line line) {
        return line.findStationsOrderUpToDown()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return createLineResponses(persistLines);
    }

    private List<LineResponse> createLineResponses(List<Line> lines) {
        return lines.stream()
                .map(line -> LineResponse.of(line, createStationResponses(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("노선이 존재하지 않습니다. 노선 ID:" + id));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations = createStationResponses(persistLine);
        return LineResponse.of(persistLine, stations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = new Section(upStation, downStation, request.getDistance());
        line.addSection(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);
    }
}
