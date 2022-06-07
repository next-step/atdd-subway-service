package nextstep.subway.line.application;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line line = request.toLine(findStationById(request.getUpStationId()),
                findStationById(request.getDownStationId()));
        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        Line line = lineUpdateRequest.toLine(findStationById(lineUpdateRequest.getUpStationId()),
                findStationById(lineUpdateRequest.getDownStationId()));
        persistLine.update(line);
        lineRepository.save(persistLine);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        line.addSection(request.toSection(line, findStationById(request.getUpStationId()),
                findStationById(request.getDownStationId())));
        lineRepository.save(line);
    }

    @Transactional
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

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException("등록된 노선이 없습니다."));
    }

    private Station findStationById(long id) {
        return stationService.findById(id);
    }
}
