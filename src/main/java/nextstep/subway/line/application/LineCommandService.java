package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationQueryService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineCommandService {
    private final StationQueryService stationQueryService;
    private final LineQueryService lineQueryService;
    private final LineRepository lineRepository;

    public LineCommandService(StationQueryService stationQueryService, LineQueryService lineQueryService, LineRepository lineRepository) {
        this.stationQueryService = stationQueryService;
        this.lineQueryService = lineQueryService;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationQueryService.findStationById(request.getUpStationId());
        Station downStation = stationQueryService.findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance(), request.getCharge()));
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineQueryService.findLineById(id);
        persistLine.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = lineQueryService.findLineById(lineId);
        Station upStation = stationQueryService.findStationById(request.getUpStationId());
        Station downStation = stationQueryService.findStationById(request.getDownStationId());
        line.addSection(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineQueryService.findLineById(lineId);
        Station station = stationQueryService.findStationById(stationId);
        line.removeStation(station);
    }
}
