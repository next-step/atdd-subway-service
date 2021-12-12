package nextstep.subway.line.application;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.common.exception.*;
import nextstep.subway.fare.*;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.station.domain.*;

@Service
@Transactional
public class LineService {
    private static final String LINE = "노선";

    private final LineReadService lineReadService;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineReadService lineReadService, LineRepository lineRepository,
        StationRepository stationRepository) {
        this.lineReadService = lineReadService;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line newLine = lineRepository.save(Line.of(
            request.getName(),
            request.getColor(),
            lineReadService.getUpStation(request.getUpStationId()),
            lineReadService.getDownStation(request.getDownStationId()),
            Distance.from(request.getDistance()),
            Fare.from(request.getExtraFare()))
        );
        return LineResponse.of(newLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineReadService.findLineById(id).update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = lineReadService.findLineById(lineId);
        Station upStation = lineReadService.getUpStation(request.getUpStationId());
        Station downStation = lineReadService.getDownStation(request.getDownStationId());
        line.addSection(upStation, downStation, Distance.from(request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineReadService.findLineById(lineId);
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException(LINE));
        line.removeLineStation(station);
    }
}
