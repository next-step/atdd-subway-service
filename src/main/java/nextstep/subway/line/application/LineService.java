package nextstep.subway.line.application;

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

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        final Line persistLine = lineRepository.save(
            new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));

        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(toList());
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(final Long id) {
        return LineResponse.of(findLineById(id));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        final Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(final Long lineId, final SectionRequest request) {
        final Line line = findLineById(lineId);
        final Station upStation = stationService.findStationById(request.getUpStationId());
        final Station downStation = stationService.findStationById(request.getDownStationId());
        final List<Station> stations = line.getStations();
        final boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        final boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

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

    public void removeLineStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        if (line.getSections().size() <= 1) {
            throw new RuntimeException();
        }

        final Optional<Section> upLineStation = line.getSections().stream()
            .filter(it -> it.getUpStation() == station)
            .findFirst();
        final Optional<Section> downLineStation = line.getSections().stream()
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
}
