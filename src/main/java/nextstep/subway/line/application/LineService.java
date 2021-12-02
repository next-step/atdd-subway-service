package nextstep.subway.line.application;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private static final String ALREADY_REGISTER_SECTION_EXCEPTION_STATEMENT = "이미 등록된 구간 입니다.";
    private static final String CANNOT_REGISTER_SECTION_EXCEPTION_STATEMENT = "등록할 수 없는 구간 입니다.";

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        return LineResponse.of(
            lineRepository.save(Line.of(
                request.getName(),
                request.getColor(),
                stationService.findById(request.getUpStationId()),
                stationService.findById(request.getDownStationId()),
                request.getDistance())
            )
        );
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.findById(id)
            .orElseThrow(RuntimeException::new)
            .update(Line.from(
                lineUpdateRequest)
            );
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        List<Station> stations = line.getStations();

        validate(upStation, downStation, stations);

        if (stations.isEmpty()) {
            line.getSections().add(Section.of(line, upStation, downStation, request.getDistance()));
            return;
        }

        if (isStationExisted(upStation, stations)) {
            addLineBaseOfUpStation(request, line, upStation, downStation);
            return;
        }

        if (isStationExisted(downStation, stations)) {
            addLineBaseOfDownStation(request, line, upStation, downStation);
            return;
        }

        throw new RuntimeException();
    }

    private void addLineBaseOfUpStation(final SectionRequest request, final Line line, final Station upStation, final Station downStation) {
        line.getSections().stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst()
            .ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));

        line.getSections()
            .add(Section.of(line, upStation, downStation, request.getDistance()));
    }

    private void addLineBaseOfDownStation(final SectionRequest request, final Line line, final Station upStation, final Station downStation) {
        line.getSections().stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst()
            .ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));

        line.getSections().add(Section.of(line, upStation, downStation, request.getDistance()));
    }

    private void validate(final Station upStation, final Station downStation, final List<Station> stations) {
        if (isStationExisted(upStation, stations) && isStationExisted(downStation, stations)) {
            throw new RuntimeException(ALREADY_REGISTER_SECTION_EXCEPTION_STATEMENT);
        }

        if (!stations.isEmpty() && isNotStationExisted(upStation, stations) &&
            isNotStationExisted(downStation, stations)) {
            throw new RuntimeException(CANNOT_REGISTER_SECTION_EXCEPTION_STATEMENT);
        }
    }

    private boolean isStationExisted(final Station station, final List<Station> stations) {
        return stations.stream().anyMatch(it -> it == station);
    }

    private boolean isNotStationExisted(final Station station, final List<Station> stations) {
        return stations.stream().noneMatch(it -> it == station);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        line.removeLineStation(stationService.findStationById(stationId));
    }
}
