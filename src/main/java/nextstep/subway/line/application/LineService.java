package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        Section section = createSection(request, persistLine);
        persistLine.addSection(section);
        return LineResponse.of(persistLine);
    }

    public LineResponse addLineStation(Long lineId, SectionRequest request) {
        Line persistLine = lineRepository.findByIdElseThrow(lineId);
        Section section = createSection(request, persistLine);
        persistLine.addSection(section);
        return LineResponse.of(persistLine);
    }

    private Section createSection(SectionRequest request, Line line) {
        Station upStation = stationRepository.findByIdElseThrow(request.getUpStationId());
        Station downStation = stationRepository.findByIdElseThrow(request.getDownStationId());
        return new Section(line, upStation, downStation, request.getDistance());
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = lineRepository.findByIdElseThrow(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findByIdElseThrow(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getSurcharge()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line persistLine = lineRepository.findByIdElseThrow(lineId);
        Station station = stationRepository.findByIdElseThrow(stationId);
        persistLine.remove(station);
    }
}
