package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.NoDataException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return persistLine.convertLineResponse();
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return persistLines.stream()
                .map(Line::convertLineResponse)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(NoDataException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        return findLineById(id).convertLineResponse();
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        line.addStation(upStation, downStation, request.getDistance());
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeStation(station);
    }

    public List<Line> fineLinesByStations(Station upStation, Station downStation) {
        List<Section> sectionsByUpStation = sectionRepository.findByUpStationOrDownStation(upStation, upStation);
        List<Section> sectionsByDownStation = sectionRepository.findByUpStationOrDownStation(downStation, downStation);
        List<Section> sections = new ArrayList<>();
        sections.addAll(sectionsByUpStation);
        sections.addAll(sectionsByDownStation);

        return sections.stream()
            .map(Section::getLine)
            .distinct()
            .collect(Collectors.toList());
    }
}
