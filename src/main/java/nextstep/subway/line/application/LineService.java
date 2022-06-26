package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
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

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationService.findStationById(request.getUpStationId());
        final Station downStation = stationService.findStationById(request.getDownStationId());
        final Line persistLine = lineRepository.save(
                new Line(
                        request.getName(),
                        request.getColor(),
                        upStation,
                        downStation,
                        request.getDistance()));
        return getLineResponseByLine(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(this::getLineResponseByLine)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return getLineResponseByLine(persistLine);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line persistLine = findLineById(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(final Long lineId, final SectionRequest request) {
        final Line line = findLineById(lineId);
        line.addSection(
                stationService.findStationById(request.getUpStationId()),
                stationService.findStationById(request.getDownStationId()),
                request.getDistance());
    }

    public void removeLineStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    private LineResponse getLineResponseByLine(final Line line) {
        return LineResponse.of(line, stationService.convertToStationResponses(line.getStations()));
    }

    private Line findLineById(final Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
