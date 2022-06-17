package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineStationServiceFacade {

    private final LineService lineService;
    private final StationService stationService;

    public LineStationServiceFacade(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineService.save(
            new Line(request.getName(), request.getColor(), upStation, downStation,
                request.getDistance()));

        List<StationResponse> stations = persistLine.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return LineResponse.of(persistLine, stations);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = lineService.findById(lineId);
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        line.addStation(upStation, downStation, request.getDistance());
    }

    @Transactional
    public void removeLineStation(Long lineId, long stationId) {
        Line line = lineService.findById(lineId);
        Station station = stationService.findById(stationId);

        line.removeStation(station);
    }
}
