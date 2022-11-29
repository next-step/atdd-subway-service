package nextstep.subway.line.application;

import javassist.NotFoundException;
import nextstep.subway.ErrorMessage;
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
        Line persistLine = lineRepository.save(createLineFromRequest(request));
        return LineResponse.of(persistLine);
    }
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) throws NotFoundException {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.notFoundLine(id)));
    }


    public LineResponse findLineResponseById(Long id) throws NotFoundException {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }
    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) throws NotFoundException {
        Line persistLine = findLineById(id);
        persistLine.update(lineUpdateRequest.toUpdateLineInfo());
    }
    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) throws NotFoundException {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Section section = request.toSection(line, upStation, downStation);
        line.addSection(section);
    }
    @Transactional
    public void removeLineStation(Long lineId, Long stationId) throws NotFoundException {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }
    private Line createLineFromRequest(LineRequest request){
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line line = request.toLine(upStation, downStation);
        return line;
    }
}
