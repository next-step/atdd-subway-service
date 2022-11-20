package nextstep.subway.line.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineExceptionCode;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository,
            StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toLineWithSection(upStation, downStation));

        return LineResponse.of(persistLine,
                StationResponse.toStationResponses(persistLine.getSortedStations()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.toLineResponses(persistLines);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(LineExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine,
                StationResponse.toStationResponses(persistLine.getSortedStations()));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        List<Section> matchedSections = sectionRepository.findAllByRequestedSection(upStation, downStation);
        Line line = findLineById(lineId);

        line.updateSections(sectionRequest.toSection(upStation, downStation), matchedSections);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Optional<Section> sectionOfUpStation = sectionRepository.findByUpStationId(stationId);
        Optional<Section> sectionOfDownStation = sectionRepository.findByDownStationId(stationId);
        Line line = findLineById(lineId);

        line.deleteSectionContainsStation(sectionOfUpStation, sectionOfDownStation);
    }
}
