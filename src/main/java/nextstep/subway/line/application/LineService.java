package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
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
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(Line.builder(request.getName(), request.getColor(), upStation, downStation, Distance.valueOf(request.getDistance()))
                .build());
        List<StationResponse> stations = getStations(persistLine).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(line -> {
                    List<StationResponse> stations = getStations(line).stream()
                            .map(StationResponse::of)
                            .collect(Collectors.toList());
                    return LineResponse.of(line, stations);
                })
                .collect(Collectors.toList());
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
        Station upStation = findStationById(lineUpdateRequest.getUpStationId());
        Station downStation = findStationById(lineUpdateRequest.getDownStationId());
        persistLine.update(Line.builder(lineUpdateRequest.getName(), lineUpdateRequest.getColor(),  upStation, downStation, Distance.valueOf(lineUpdateRequest.getDistance()))
                .build());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        line.addSection(request.toSection(line, findStationById(request.getUpStationId()), findStationById(request.getDownStationId())));
        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);
        if (line.sections().sections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = line.sections().sections().stream()
                .filter(it -> it.upStation() == station)
                .findFirst();
        Optional<Section> downLineStation = line.sections().sections().stream()
                .filter(it -> it.downStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().upStation();
            Station newDownStation = upLineStation.get().downStation();
            int newDistance = upLineStation.get().distance().distance() + downLineStation.get().distance().distance();
            line.sections().add(Section.builder(line, newUpStation, newDownStation, Distance.valueOf(newDistance))
                    .build());
        }

        upLineStation.ifPresent(it -> line.sections().sections().remove(it));
        downLineStation.ifPresent(it -> line.sections().sections().remove(it));
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException("등록된 노선이 없습니다."));
    }

    private Station findStationById(long id) {
        return stationService.findById(id);
    }

    public List<Station> getStations(Line line) {
        if (line.sections().sections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.sections().sections().stream()
                    .filter(it -> it.upStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().downStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(Line line) {
        Station downStation = line.sections().sections().get(0).upStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.sections().sections().stream()
                    .filter(it -> it.downStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().upStation();
        }

        return downStation;
    }
}
