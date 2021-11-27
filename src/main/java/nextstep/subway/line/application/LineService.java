package nextstep.subway.line.application;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, Distance.of(request.getDistance())));

        return LineResponse.of(persistLine, getStationsBy(persistLine));
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                                .map(line -> LineResponse.of(line, getStationsBy(line)))
                                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = lineRepository.findById(id)
                                        .orElseThrow(() -> new NoSuchElementException("조회되는 라인이 없습니다."));

        return LineResponse.of(persistLine, getStationsBy(persistLine));
    }

    private List<StationResponse> getStationsBy(Line line) {
        return line.findStations().stream()
                    .map(StationResponse::of)
                    .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id)
                                           .orElseThrow(() -> new NoSuchElementException("조회되는 라인이 없습니다."));

        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId)
                                    .orElseThrow(() -> new NoSuchElementException("조회되는 라인이 없습니다."));

        Section section = generateSection(line, request);

        line.addSection(section);
    }

    private Section generateSection(Line line, SectionRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        return new Section(line, upStation, downStation, Distance.of(request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                                    .orElseThrow(() -> new NoSuchElementException("조회되는 라인이 없습니다."));

        Station station = stationService.findStationById(stationId);

        line.deleteStation(station);
    }

    public Sections findAllSections() {
        Sections sections = Sections.of();

        for (Line line : lineRepository.findAll()) {
            sections.add(line.getSections());
        }

        return sections;
    }
}
