package nextstep.subway.line.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
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

    public LineResponse saveLine(LineCreateRequest request) {
        return LineResponse.of(savedLine(request));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(line(id));
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        line(id).update(request.name(), request.color());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        line(lineId).addSection(section(request));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = line(lineId);
        Station station = stationService.findStationById(stationId);
        if (line.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = line.getSections().stream()
            .filter(it -> it.upStation() == station)
            .findFirst();
        Optional<Section> downLineStation = line.getSections().stream()
            .filter(it -> it.downStation() == station)
            .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().upStation();
            Station newDownStation = upLineStation.get().downStation();
            Distance newDistance =
                upLineStation.get().distance().sum(downLineStation.get().distance());
            line.getSections()
                .add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> line.getSections().remove(it));
        downLineStation.ifPresent(it -> line.getSections().remove(it));
    }

    private Line savedLine(LineCreateRequest request) {
        return lineRepository.save(
            Line.of(request.name(),
                request.color(),
                Sections.from(section(request.getSection()))
            )
        );
    }

    private Section section(SectionRequest request) {
        return Section.of(
            station(request.getUpStationId()),
            station(request.getDownStationId()),
            Distance.from(request.getDistance())
        );
    }

    private Station station(Long stationId) {
        return stationService.findById(stationId);
    }

    private Line line(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("line(%d) is not exist", id)));
    }
}
