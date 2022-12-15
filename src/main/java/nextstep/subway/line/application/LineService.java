package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionCreateRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest request) {
        Line persistLine = lineRepository.save(mapToLine(request));
        return LineResponse.of(persistLine);
    }

    private Line mapToLine(LineCreateRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        return new Line(request.getName(),
                request.getColor(),
                upStation,
                downStation,
                request.getDistance(),
                request.getFare());
    }

    public List<LineResponse> findLines() {
        return LineResponse.list(lineRepository.findAllWithSections());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    @Transactional
    public void updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
        Line persistLine = findLineById(lineId);
        persistLine.update(lineUpdateRequest.toLine());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionCreateRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findByIdWithSections(id)
                .orElseThrow(RuntimeException::new);
    }
}
