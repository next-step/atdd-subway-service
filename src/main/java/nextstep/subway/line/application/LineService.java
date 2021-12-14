package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        final Line persistLine = lineRepository.save(
            new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance())
        );
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = getLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line persistLine = getLineById(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(final Long lineId, final SectionRequest request) {
        final Line line = getLineById(lineId);
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());

        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeLineStation(final Long lineId, final Long stationId) {
        final Line line = getLineById(lineId);
        final Station targetStation = stationService.findById(stationId);

        line.removeStation(targetStation);
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("지하철 노선을 찾을 수 없습니다."));
    }
}
