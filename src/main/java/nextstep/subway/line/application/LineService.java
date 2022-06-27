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

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toEntity(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
            .map(LineResponse::of)
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

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        Section targetSection = Section.of(line, upStation, downStation, request.getDistance());
        line.addSection(targetSection);
    }

    public void renewal_removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);
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
