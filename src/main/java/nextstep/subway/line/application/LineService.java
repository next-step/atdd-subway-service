package nextstep.subway.line.application;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
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

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        final Station upStation = stationService.findStationByIdAsDomainEntity(request.getUpStationId());
        final Station downStation = stationService.findStationByIdAsDomainEntity(request.getDownStationId());
        final Line persistLine = lineRepository.save(new Line(
            request.getName(),
            request.getColor(),
            upStation,
            downStation,
            request.getDistance()));
        return LineResponse.from(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        final Line line = findLineByIdAsDomainEntity(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        final List<LineResponse> result = lineRepository.findAll()
            .stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.from(findLineByIdAsDomainEntity(id));
    }

    @Transactional(readOnly = true)
    public Line findLineByIdAsDomainEntity(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        final Line line = findLineByIdAsDomainEntity(lineId);
        final Station upStation = stationService.findStationByIdAsDomainEntity(request.getUpStationId());
        final Station downStation = stationService.findStationByIdAsDomainEntity(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, Distance.from(request.getDistance())));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        final Line line = findLineByIdAsDomainEntity(lineId);
        final Station station = stationService.findStationByIdAsDomainEntity(stationId);
        line.removeLineStation(station);
    }
}
