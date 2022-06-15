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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line savedLine = lineRepository.save(
                new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(), request.getExtraCharge()));
        return LineResponse.of(savedLine);
    }

    public List<LineResponse> findLines() {
        List<Line> findLines = lineRepository.findAll();
        return findLines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }


    public LineResponse findLineResponseById(Long id) {
        Line findLine = findLineById(id);
        return LineResponse.of(findLine);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line findLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        findLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line findLine = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        findLine.addSection(new Section(upStation, downStation, request.getDistance()));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line findLine = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        if (findLine.getSections().size() <= 1) {
            throw new RuntimeException();
        }
        findLine.deleteStation(station);
    }
}
