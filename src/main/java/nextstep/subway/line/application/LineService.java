package nextstep.subway.line.application;

import nextstep.subway.ErrorMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(),
                stationService.findStationById(request.getUpStationId()),
                stationService.findStationById(request.getDownStationId()),
                new Distance(request.getDistance())));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLinesResponse() {
        return findLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(ErrorMessage.DO_NOT_EXIST_LINE_ID.getMessage()));
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        findLineById(id).update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        line.addSection(new Section(line,
                stationService.findStationById(request.getUpStationId()),
                stationService.findStationById(request.getDownStationId()),
                new Distance(request.getDistance())));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        findLineById(lineId).removeStation(stationService.findStationById(stationId));
    }

}
