package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
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
        validateDuplicateName(request.name());
        return LineResponse.from(savedLine(request));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        return LineResponse.from(line(id));
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        validateDuplicateName(request.name());
        line(id).update(request.name(), request.color());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        line(lineId).addSection(section(request));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        line(lineId).removeStation(station(stationId));
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

    private void validateDuplicateName(Name name) {
        if (lineRepository.existsByName(name)) {
            throw new DuplicateDataException(String.format("이름(%s)은 이미 존재합니다.", name));
        }
    }
}
