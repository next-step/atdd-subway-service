package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
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
        return LineResponse.listOf(findAll());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(long id) {
        return LineResponse.from(line(id));
    }

    public void updateLine(long id, LineUpdateRequest request) {
        validateDuplicateName(request.name());
        line(id).update(request.name(), request.color());
    }

    public void deleteLineById(long id) {
        lineRepository.delete(line(id));
    }

    public void addLineStation(long lineId, SectionRequest request) {
        line(lineId).addSection(section(request));
    }

    public void removeLineStation(long lineId, long stationId) {
        line(lineId).removeStation(station(stationId));
    }

    @Transactional(readOnly = true)
    public Lines findAll() {
        return lineRepository.findAllLines();
    }

    private Line savedLine(LineCreateRequest request) {
        return lineRepository.save(newLine(request));
    }

    private Line newLine(LineCreateRequest request) {
        if (request.hasExtraFare()) {
            return Line.of(request.name(), request.color(),
                Sections.from(section(request.getSection())), request.extraFare());
        }
        return Line.of(
            request.name(), request.color(), Sections.from(section(request.getSection())));
    }

    private Section section(SectionRequest request) {
        return Section.of(
            station(request.getUpStationId()),
            station(request.getDownStationId()),
            request.distance()
        );
    }

    private Station station(long stationId) {
        return stationService.findById(stationId);
    }

    private Line line(long id) {
        return lineRepository.findById(id)
            .orElseThrow(() ->
                new NotFoundException(String.format("지하철 노선 id(%d) 존재하지 않습니다.", id)));
    }

    private void validateDuplicateName(Name name) {
        if (lineRepository.existsByName(name)) {
            throw new DuplicateDataException(String.format("지하철 노선 이름(%s)은 이미 존재합니다.", name));
        }
    }
}
