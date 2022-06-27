package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    /**
     * 신규 노선 저장
     */
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository
            .save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        List<StationResponse> stations = getStations(persistLine).stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    /**
     * 노선 목록 조회
     */
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
            .map(line -> {
                List<StationResponse> stations = getStations(line).stream()
                    .map(it -> StationResponse.of(it))
                    .collect(Collectors.toList());
                return LineResponse.of(line, stations);
            })
            .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    /**
     * 특정 노선 조회
     */
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations = getStations(persistLine).stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    /**
     * 특정 노선 정보 수정
     */
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    /**
     * 특정 노선 삭제
     */
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    /**
     * 구간 추가
     */
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
            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
            return;
        }

        if (isUpStationExisted) {
            line.getSections().stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));

            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
        } else if (isDownStationExisted) {
            line.getSections().stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));

            line.getSections().add(new Section(line, upStation, downStation, request.getDistance()));
        } else {
            throw new RuntimeException();
        }
    }

    public void renewal_addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        Section targetSection = new Section(line, upStation, downStation, request.getDistance());
        line.addSection(targetSection);
    }

    /**
     * 구간 삭제
     */
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        if (line.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = line.getSections().stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        Optional<Section> downLineStation = line.getSections().stream()
            .filter(it -> it.getDownStation() == station)
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            line.getSections().add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> line.getSections().remove(it));
        downLineStation.ifPresent(it -> line.getSections().remove(it));
    }

    /**
     * 노선의 정렬된 역 목록을 조회한다.
     * <p>
     * - 상행종점역 구간을 기준으로 기준 구간의 하행역이 다른 구간의 상행역이 아닐때까지 전체 구간을 순회하며 정렬된 역 목록을 구성한다.
     */
    private List<Station> getStations(Line line) {
        if (line.getSections().isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    /**
     * 노선의 상행종점역을 조회한다.
     * <p>
     * - 전체 구간을 순회하며 상행역이 다른 구간의 하행역이 아닌 구간의 상행역을 반환한다.
     */
    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation(); // 양재
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation(); // 강남 -> 신논현 -> 논현
        }

        return downStation; // 논현
    }
}
