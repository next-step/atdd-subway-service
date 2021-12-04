package nextstep.subway.line.application;

import nextstep.subway.exception.IsEqualsTwoStationsException;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPathsBySection(PathRequest request) {
        Sections sections = new Sections(sectionRepository.findAll());
        isEqualsStations(request);
        Station sourceStation = stationRepository.findByIdElseThrow(request.getSource());
        Station targetStation = stationRepository.findByIdElseThrow(request.getTarget());
        return sections.generatePaths(sourceStation, targetStation);
    }

    private void isEqualsStations(PathRequest request) {
        if (request.getSource().equals(request.getTarget())) {
            throw new IsEqualsTwoStationsException();
        }
    }
}
