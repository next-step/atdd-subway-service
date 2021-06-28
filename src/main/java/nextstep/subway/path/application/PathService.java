package nextstep.subway.path.application;

import nextstep.subway.common.Excetion.StationNotFoundException;
import nextstep.subway.line.collection.Sections;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findOptimalPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = findStation(sourceStationId);
        Station targetStation = findStation(targetStationId);
        Sections sections = new Sections(getAllSection());
        return PathResponse.of(new Path().findOptimalPath(sourceStation, targetStation,sections));
    }

    private List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
    }
}
