package nextstep.subway.line.application;

import nextstep.subway.exception.DataNotExistException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

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
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(Line.of(request.getName(),
                                                       request.getColor(),
                                                       upStation,
                                                       downStation,
                                                       request.getDistance()));

        return LineResponse.of(persistLine, StreamUtils.mapToList(persistLine.getStations(), StationResponse::of));
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return StreamUtils.mapToList(persistLines, this::mapToLineResponses);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(DataNotExistException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<StationResponse> stations = StreamUtils.mapToList(persistLine.getStations(), StationResponse::of);

        return LineResponse.of(persistLine, stations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(Line.of(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(Section.of(line, upStation, downStation, Distance.from(request.getDistance())));
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);

        line.removeStation(station);
    }

    private LineResponse mapToLineResponses(Line line) {
        List<StationResponse> stations = StreamUtils.mapToList(line.getStations(), StationResponse::of);
        return LineResponse.of(line, stations);
    }
}
