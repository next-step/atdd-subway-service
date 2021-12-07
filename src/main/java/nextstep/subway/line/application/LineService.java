package nextstep.subway.line.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.message.Message;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Station upStation = stationService.findStationById(request.getUpStationId());
        final Station downStation = stationService.findStationById(request.getDownStationId());
        final Sections sections = Sections.from(Section.of(upStation, downStation, Distance.of(request.getDistance())));
        final Line persistLine = lineRepository.save(Line.of(request.getName(), request.getColor(), sections));
        return LineResponse.from(persistLine);
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.from(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        final Line persistLine = findLineById(id);
        persistLine.update(Line.of(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        final Section section = Section.of(upStation, downStation, Distance.of(request.getDistance()));
        line.addSection(section);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        final Line line = findLineById(lineId);
        final Station station = stationService.findStationById(stationId);
        line.removeSectionByStationId(station);
    }

    private Line findLineById(final Long lineid) {
        return lineRepository.findById(lineid)
                .orElseThrow(() -> new NotFoundException(Message.NOT_FIND_LINE.getMessage()));
    }

}
