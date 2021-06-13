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
        Line persistLine = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        createSection(request.getUpStationId(), request.getDownStationId(), request.getDistance())
                ));

        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLines() {
        Lines lines = new Lines(lineRepository.findAll());

        return lines.toResponses();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);

        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        line.addSection(createSection(request.getUpStationId(), request.getDownStationId(), request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);

        line.removeStation(station);
    }

    private Section createSection(long upstationId, long downStationId, int distance) {
        Station upStation = stationService.findById(upstationId);
        Station downStation = stationService.findById(downStationId);

        return new Section(upStation, downStation, new Distance(distance));
    }
}
