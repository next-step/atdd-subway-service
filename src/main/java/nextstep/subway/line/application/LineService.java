package nextstep.subway.line.application;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathBag;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(isolation = READ_COMMITTED)
    public LineResponse saveLine(LineRequest request) {
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        final Line persistLine = lineRepository.save(
                new Line(request.getName(), request.getColor(), upStation, downStation,
                        new Distance(request.getDistance()), Money.from(request.getExtraCharge())));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional(isolation = READ_COMMITTED)
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        final Line persistLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 노선입니다. 요청id:" + id));
        persistLine.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional(isolation = READ_COMMITTED)
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(isolation = READ_COMMITTED)
    public void addLineStation(Long lineId, SectionRequest request) {
        final Line line = findLineById(lineId);
        final Station upStation = stationService.findStationById(request.getUpStationId());
        final Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, new Distance(request.getDistance())));
    }

    @Transactional(isolation = READ_COMMITTED)
    public void removeLineStation(Long lineId, Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 노선입니다. 요청id:" + id));
    }

    public PathBag findPathBag() {
        List<Line> lines = lineRepository.findAll();
        validEmptyLine(lines);
        return PathBag.fromLines(lines);
    }

    private void validEmptyLine(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new IllegalStateException("노선이 존재하지 않습니다");
        }
    }
}
