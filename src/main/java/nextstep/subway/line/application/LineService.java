package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationService;

    public LineService(LineRepository lineRepository, StationRepository stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(
                new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(),
                        request.getExtraFare()));
        List<StationResponse> stations =
                persistLine.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    private Station findStationById(long stationId) {
        return stationService.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    public List<LineResponse> findAllLineResponse() {
        Lines lines = findLines();
        return StreamSupport.stream(lines.spliterator(), false).map(line -> {
            List<StationResponse> stations =
                    line.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
            return LineResponse.of(line, stations);
        }).collect(Collectors.toList());
    }

    public Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations =
                persistLine.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        line.addLineStation(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);
        line.removeLineStation(station);
    }
}
