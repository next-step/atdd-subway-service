package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.constant.ErrorCode;
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

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.from(persistLine);
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.from(persistLine);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.updateNameAndColor(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        line.addSection(Section.of(line, upStation, downStation, request.getDistance()));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);
        line.removeStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.해당하는_노선_없음.getErrorMessage()));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_역.getErrorMessage()));
    }
}
