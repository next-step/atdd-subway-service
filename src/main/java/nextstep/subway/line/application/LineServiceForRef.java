package nextstep.subway.line.application;

import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineServiceForRef {
    private final LineRepository lineRepository;
    private final StationService stationService;

    private final SectionRepository sectionRepository;

    public LineServiceForRef(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        final Section savedSection = sectionRepository.save(new Section(request.toLine(), upStation, downStation, request.getDistance()));
        final Line savedLine = savedSection.getLine();
        return LineResponse.of(savedLine, stationResponsesBy(savedLine.getStations()));
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.of(line, stationResponsesBy(line.getStations())))
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations = getStations(persistLine).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
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
        Section section = new Section(findLineById(lineId), upStation, downStation, request.getDistance());
        line.addSection(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);
    }


    public List<Station> getStations(Line line) {
       return line.getStations();
    }

    private List<StationResponse> stationResponsesBy(final List<Station> stations) {
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }
}
