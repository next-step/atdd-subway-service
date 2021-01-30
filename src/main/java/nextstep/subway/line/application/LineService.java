package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineResponses;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.StationResponses;
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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(createLineByRequest(request, upStation, downStation));
        StationResponses stationResponses = StationResponses.of(persistLine.getStations());
        return LineResponse.of(persistLine, stationResponses);
    }

    private Line createLineByRequest(LineRequest request, Station upStation, Station downStation) {
        return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
    }

    public List<LineResponse> findLines() {
        Lines lines = Lines.of(lineRepository.findAll());
        LineResponses lineResponses = LineResponses.of(lines.getLine());
        return lineResponses.getLineResponses();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        StationResponses stationResponses = StationResponses.of(persistLine.getStations());
        return LineResponse.of(persistLine, stationResponses);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
       line.removeStation(station);
    }
}
