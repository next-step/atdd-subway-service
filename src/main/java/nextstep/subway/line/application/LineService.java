package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line line = new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
        Line persistLine = lineRepository.save(line);
        List<StationResponse> stations = toResponseOf(line.getStations());
        return LineResponse.of(persistLine, stations);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(line -> LineResponse.of(line, toResponseOf(line.getStations())))
                .collect(Collectors.toList());
    }

    public List<StationResponse> toResponseOf(Set<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine, toResponseOf(persistLine.getStations()));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(new Section(upStation, downStation, request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        line.deleteStationById(stationId);
    }
}
