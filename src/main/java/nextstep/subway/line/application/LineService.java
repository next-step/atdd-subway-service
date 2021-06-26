package nextstep.subway.line.application;

import java.util.List;
import java.util.Map;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line persistLine = lineRepository.save(makeLine(request));
        return LineResponse.of(persistLine);
    }

    private Line makeLine(LineRequest request) {
        Map<Long, Station> stationMap = stationService.findMapByIds(request.getUpStationId(), request.getDownStationId());
        Station upStation = stationMap.get(request.getUpStationId());
        Station downStation = stationMap.get(request.getDownStationId());
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
    }

    public List<LineResponse> findLines() {
        return LineResponse.ofList(lineRepository.findAll());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findById(id));
    }

    @Transactional(readOnly = true)
    public Line findById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findById(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line persistLine = findById(lineId);
        Section section = new Section(persistLine, upStation, downStation, request.getDistance());
        persistLine.addSection(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Station station = stationService.findStationById(stationId);
        Line line = findById(lineId);
        line.deleteStation(station);
    }
}
