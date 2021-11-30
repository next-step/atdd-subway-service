package nextstep.subway.line.application;

import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.exception.LineException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.line.domain.line.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = findAllLine();

        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findOneLine(id);

        return LineResponse.of(persistLine);
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Line persistLine = request.toLine();
        persistLine.addSection(upStation, downStation, request.getDistance());

        return LineResponse.of(lineRepository.save(persistLine));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findOneLine(id);

        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findOneLine(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findOneLine(lineId);
        Station removeStation = stationService.findStationById(stationId);

        line.removeSection(removeStation);
    }

    private List<Line> findAllLine() {
        return lineRepository.findAll();
    }

    private Line findOneLine(Long id) {
        return lineRepository.findLineWithSectionsById(id)
                .orElseThrow(() -> new LineException(ErrorCode.NOT_FOUND_ENTITY, "노선이 존재하지 않습니다."));
    }

}
