package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;


    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request, Station upStation, Station downStation) {
        Line persistLine = lineRepository.save(
            new Line(request.getName(), request.getColor(), upStation, downStation,
                request.getDistance()));

        List<StationResponse> stations = persistLine.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
            .map(line -> {
                List<StationResponse> stations = line.getStations()
                    .stream()
                    .map(StationResponse::of)
                    .collect(Collectors.toList());
                return LineResponse.of(line, stations);
            })
            .collect(Collectors.toList());
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("노선을 찾을수 없습니다"));
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findById(id);
        List<StationResponse> stations = persistLine.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findById(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request, Station upStation, Station downStation) {
        Line line = findById(lineId);
        line.addStation(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Station station) {
        Line line = findById(lineId);
        line.removeStation(station);
    }
}
