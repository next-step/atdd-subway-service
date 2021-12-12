package nextstep.subway.domain.line.application;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.LineRepository;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.line.dto.SectionRequest;
import nextstep.subway.domain.station.application.StationService;
import nextstep.subway.domain.station.domain.Station;
import nextstep.subway.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, new Distance(request.getDistance())));

        return LineResponse.of(persistLine, LineResponse.stationsToStationResponseMapper(persistLine.getStations()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(line -> LineResponse.of(line, LineResponse.stationsToStationResponseMapper(line.getStations())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);

        return LineResponse.of(persistLine, LineResponse.stationsToStationResponseMapper(persistLine.getStations()));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(upStation, downStation, new Distance(request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);

        line.removeSection(station);
    }
}
