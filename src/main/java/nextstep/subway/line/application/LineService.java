package nextstep.subway.line.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.NoSuchElementException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Line.Builder;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Cacheable("lines")
    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    @CacheEvict({"lines","graph"})
    public LineResponse saveLine(LineRequest request) {
        Builder builder = new Builder(request.getName(), request.getColor())
                .upStation(stationService.findStationById(request.getUpStationId()))
                .downStation(stationService.findStationById(request.getDownStationId()))
                .distance(request.getDistance());
        return LineResponse.of(lineRepository.save(builder.build()));
    }

    @CacheEvict({"lines","graph"})
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @CacheEvict({"lines","graph"})
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @CacheEvict({"lines","graph"})
    public void addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    @CacheEvict({"lines","graph"})
    public void removeStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.deleteStation(station);
    }
}
