package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Excetion.StationNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findOptimalPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Station sourceStation = findStation(sourceStationId);
        Station targetStation = findStation(targetStationId);
        Path path = Path.findOptimalPath(sourceStation, targetStation, sectionRepository.findAll());
        path.surCharge(loginMember, getLinesOfSection(path.getStations()));
        return PathResponse.of(path);
    }

    private List<Line> getLinesOfSection(List<Station> stations) {
        List<Line> linesOfSection = new ArrayList<>();
        for (int stationIndex = 0; stationIndex < stations.size() - 1; stationIndex++) {
            linesOfSection.add(sectionRepository.findByUpStationIdAndDownStationId(stations.get(stationIndex).getId(), stations.get(stationIndex + 1).getId()).getLine());
        }
        return linesOfSection;
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
    }
}
