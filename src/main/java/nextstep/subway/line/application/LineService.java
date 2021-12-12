package nextstep.subway.line.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.application.exception.LineErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.infrastructure.line.LineRepository;
import nextstep.subway.line.dto.line.LineRequest;
import nextstep.subway.line.dto.line.LineResponse;
import nextstep.subway.line.dto.section.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(mapLine(request));

        return LineResponse.of(persistLine);
    }


    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.toList(persistLines);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> NotFoundException.of(LineErrorCode.LINE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Line mapLine(LineRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        return request.toLine(upStation, downStation);
    }
}
