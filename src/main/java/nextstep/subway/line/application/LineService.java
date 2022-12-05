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
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository,
            StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toLineWithSection(upStation, downStation));

        return LineResponse.of(persistLine,
                StationResponse.toStationResponses(persistLine.getSortedStations()));
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException(StationExceptionCode.NOT_FOUND_BY_ID.getMessage()));
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
    public void updateLine(Long id, LineRequest lineRequest) {
        Line persistLine = findLineById(id);
        persistLine.update(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getFare()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = findStationById(sectionRequest.getUpStationId());
        Station downStation = findStationById(sectionRequest.getDownStationId());
        List<Section> matchedSections = sectionRepository.findAllByRequestedSection(upStation, downStation);
        Line line = findLineById(lineId);

        line.updateSections(sectionRequest.toSection(line, upStation, downStation), matchedSections);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Optional<Section> sectionOfUpStation = sectionRepository.findByUpStationId(stationId);
        Optional<Section> sectionOfDownStation = sectionRepository.findByDownStationId(stationId);
        Line line = findLineById(lineId);

        line.deleteSectionContainsStation(sectionOfUpStation, sectionOfDownStation);
    }
}
