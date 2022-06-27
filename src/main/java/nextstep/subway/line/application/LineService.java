package nextstep.subway.line.application;

import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        validateDuplicate(request.getName(), request.getColor());

        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));

        return LineResponse.of(persistLine);
    }

    private void validateDuplicate(String name, String color) {
        validateDuplicatedName(name);
        validateDuplicatedColor(color);
    }

    private void validateDuplicatedName(String name) {
        Optional<Line> lineByName = lineRepository.findByName(new LineName(name));

        lineByName.ifPresent(line -> {
            throw new IllegalArgumentException("중복된 지하철 노선 이름입니다.");
        });
    }

    private void validateDuplicatedColor(String color) {
        Optional<Line> lineByColor = lineRepository.findByColor(new LineColor(color));

        lineByColor.ifPresent(line -> {
            throw new IllegalArgumentException("중복된 지하철 노선 색갈입니다.");
        });
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = findAllLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철노선입니다."));
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);

        if (!Objects.equals(persistLine.getName().getValue(), lineUpdateRequest.getName())) {
            validateDuplicatedName(lineUpdateRequest.getName());
        }

        if (!Objects.equals(persistLine.getColor().getValue(), lineUpdateRequest.getColor())) {
            validateDuplicatedColor(lineUpdateRequest.getColor());
        }

        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        line.addLineSection(upStation, downStation, new Distance(request.getDistance()));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findById(stationId);

        line.removeStation(station.getId());
    }
}
