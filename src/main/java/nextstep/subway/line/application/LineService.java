package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());
        final Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(),
            request.getExtraFare(), upStation, downStation, request.getDistance()));
        final List<StationResponse> stations = stationResponses(persistLine);

        return LineResponse.of(persistLine, stations);
    }

    private Station findStationById(final Long upStationId) {
        return stationService.findById(upStationId);
    }

    private List<StationResponse> stationResponses(final Line persistLine) {
        return persistLine.stations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();

        return persistLines.stream()
            .map(line -> LineResponse.of(line, stationResponses(line)))
            .collect(Collectors.toList());
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        final List<StationResponse> stations = stationResponses(persistLine);

        return LineResponse.of(persistLine, stations);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line persistLine = lineRepository.findById(id)
            .orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(final Long lineId, final SectionRequest request) {
        final Line line = findLineById(lineId);
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeLineStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = findStationById(stationId);
        line.removeSection(station);
    }
}
