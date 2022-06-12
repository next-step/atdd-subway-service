package nextstep.subway.line.application;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line line = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.from(line);
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.generateLineResponses(lines);
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = findLineById(id);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                String.format(ErrorMessage.ERROR_LINE_NOT_FOUND, id))
        );
        line.update(Line.of(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(Section.of(line, upStation, downStation, request.getDistance()));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                String.format(ErrorMessage.ERROR_LINE_NOT_FOUND, id))
        );
    }
}
