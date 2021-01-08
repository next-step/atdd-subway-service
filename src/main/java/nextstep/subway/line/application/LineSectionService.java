package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@Service
@Transactional
public class LineSectionService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineSectionService(LineRepository lineRepository,
            StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public void addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);

        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Section section = new Section(line, upStation, downStation, request.getDistance());
        line.addSection(section);
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findById(stationId);

        line.removeSection(line, station);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

}
