package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));

        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.ofList(persistLines);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(lineUpdateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        Line line = findLineById(id);
        lineRepository.delete(line);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        Section section =
            Section.create(upStation, downStation, Distance.valueOf(request.getDistance()));

        line.addLineStation(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStation(stationId);

        line.removeLineStation(station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당하는 노선이 없습니다."));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException("해당하는 지하철역이 없습니다."));
    }
}
