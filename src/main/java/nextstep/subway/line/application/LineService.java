package nextstep.subway.line.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(), request.getSurcharge()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.listOf(persistLines);
    }

    public LineResponse findLineById(Long id) {
        Line persistLine = lineRepository.findById(id).orElseThrow(() -> new NotFoundException("요청 노선이 존재하지 않음 : " + id));;
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(() -> new NotFoundException("요청 노선이 존재하지 않음 : " + id));;
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addLineSection(upStation, downStation, request.getDistance());
    }

    public void removeLineSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new NotFoundException("요청 노선이 존재하지 않음 : " + lineId));;
        Station station = stationService.findStationById(stationId);
        line.removeLineSection(station);
    }
}
