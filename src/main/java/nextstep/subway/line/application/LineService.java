package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        final Line saveLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(saveLine);
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.ofLines(lines);
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineException(LineExceptionType.LINE_NOT_FOUND));
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line line = findLineById(id);
        return LineResponse.of(line);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line line = findLineById(id);
        line.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(final Long lineId, final SectionRequest request) {
        final Line line = findLineById(lineId);
        final Station upStation = stationService.findStationById(request.getUpStationId());
        final Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeLineStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        line.deleteStation(station);
    }

}
