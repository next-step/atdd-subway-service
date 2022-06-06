package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
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
        Station upStation = stationService.findById(lineUpdateRequest.getUpStationId());
        Station downStation = stationService.findById(lineUpdateRequest.getDownStationId());
        persistLine.update(Line.builder(lineUpdateRequest.getName(), lineUpdateRequest.getColor(),  upStation, downStation, Distance.valueOf(lineUpdateRequest.getDistance()))
                .build());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        List<Station> stations = getStations(line);
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
                stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            line.sections().add(Section.builder(line, upStation, downStation, Distance.valueOf(request.getDistance()))
                    .build());
            return;
        }

        if (isUpStationExisted) {
            line.sections().sections().stream()
                    .filter(it -> it.upStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, Distance.valueOf(request.getDistance())));

            line.sections().add(Section.builder(line, upStation, downStation, Distance.valueOf(request.getDistance()))
                    .build());
        } else if (isDownStationExisted) {
            line.sections().sections().stream()
                    .filter(it -> it.downStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, Distance.valueOf(request.getDistance())));

            line.sections().add(Section.builder(line, upStation, downStation, Distance.valueOf(request.getDistance()))
                    .build());
        } else {
            throw new RuntimeException();
        }
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
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
