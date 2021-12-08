package nextstep.subway.line.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        List<StationResponse> stations = makeStationResponses(persistLine);
        return LineResponse.of(persistLine, stations);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return makeLineResponses(persistLines);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations = makeStationResponses(persistLine);
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
        addLineStation(request, line, upStation, downStation);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        removeLineStation(line, station);
    }

    private List<StationResponse> makeStationResponses(Line line) {
        return line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private List<LineResponse> makeLineResponses(List<Line> lines) {
        return lines.stream()
            .map(line -> {
                List<StationResponse> stations = makeStationResponses(line);
                return LineResponse.of(line, stations);
            })
            .collect(Collectors.toList());
    }

    private void addLineStation(SectionRequest request, Line line, Station upStation, Station downStation) {
        List<Station> stations = line.getStations();
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
            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
            return;
        }

        if (isUpStationExisted) {
            line.getSections().stream()
                .filter(it -> it.hasUpStation(upStation))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));

            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
        } else if (isDownStationExisted) {
            line.getSections().stream()
                    .filter(it -> it.hasDownStation(downStation))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));

            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
        } else {
            throw new RuntimeException();
        }
    }

    private void removeLineStation(Line line, Station station) {
        if (line.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = line.getSections().stream()
            .filter(it -> it.hasUpStation(station))
                .findFirst();
        Optional<Section> downLineStation = line.getSections().stream()
            .filter(it -> it.hasDownStation(station))
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section section = Section.combine(downLineStation.get(), upLineStation.get());
            line.getSections().add(section);
        }

        upLineStation.ifPresent(it -> line.getSections().remove(it));
        downLineStation.ifPresent(it -> line.getSections().remove(it));
    }
}
