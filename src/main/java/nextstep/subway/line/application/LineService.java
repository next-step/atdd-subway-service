package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(createPersistLineOfRequest(request));
        return LineResponse.of(persistLine);
    }

    private Line createPersistLineOfRequest(LineRequest request) {
        return new Line(request.getName(),
                request.getColor(),
                stationService.findStationById(request.getUpStationId()),
                stationService.findStationById(request.getDownStationId()),
                request.getDistance(),
                request.getOverFare());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return findLine(id);
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        findLine(id).update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    private Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("id: {%s} 노선을 찾을수 없습니다.", id)));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        findLineById(lineId).add(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Station station = stationService.findStationById(stationId);
        findLineById(lineId).removeStation(station);
    }
}
