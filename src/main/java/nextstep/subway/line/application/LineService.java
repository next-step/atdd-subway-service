package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());
        final Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        final List<Line> persistLines = lineRepository.findAll();
        return LineResponse.ofList(persistLines);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long lineId, SectionRequest request) {
        final Line line = findLineById(lineId);
        final Station upStation = findStationById(request.getUpStationId());
        final Station downStation = findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());
    }

    public void deleteSection(Long lineId, Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = findStationById(stationId);
        line.deleteSection(station);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }
}
