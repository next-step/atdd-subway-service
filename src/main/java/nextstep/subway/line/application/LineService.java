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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findStationByIdOrElseThrow(request.getUpStationId());
        Station downStation = stationService.findStationByIdOrElseThrow(request.getDownStationId());
        Line line = lineRepository.save(
                new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(), request.getSurcharge()));
        return LineResponse.of(line);
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findLineById(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = findLineById(id);
        line.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationByIdOrElseThrow(request.getUpStationId());
        Station downStation = stationService.findStationByIdOrElseThrow(request.getDownStationId());
        line.addLineStation(upStation, downStation, request.getDistance());
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationByIdOrElseThrow(stationId);
        line.removeLineStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("라인을 찾을 수 없습니다. id = " + id));
    }

    public Sections findAllSections() {
        return new Sections(sectionRepository.findAll());
    }


}
