package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.impl.FarePolicyByAge;
import nextstep.subway.path.domain.impl.FarePolicyByDistance;
import nextstep.subway.path.domain.impl.FarePolicyByLine;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPath(LoginMember loginMember, long sourceId, long targetId) {
        Path path = getPath(sourceId, targetId);

        List<FarePolicy> farePolicies = new ArrayList<>();
        farePolicies.add(path.getFarePolicyByLine());
        farePolicies.add(path.getFarePolicyByDistance());
        farePolicies.add(FarePolicyByAge.findCategory(loginMember.getAge()));

        Fare fare = Fare.of(farePolicies);

        List<StationResponse> stationResponses = path.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        return PathResponse.of(stationResponses, path, fare);
    }

    public PathResponse findPath(long sourceId, long targetId) {
        Path path = getPath(sourceId, targetId);
        List<StationResponse> stationResponses = path.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        return PathResponse.of(stationResponses, path, new Fare());
    }

    private Path getPath(long sourceId, long targetId) {
        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);

        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        PathFinder pathFinder = PathFinder.of(stations, sections);
        return pathFinder.getPath(sourceStation, targetStation);
    }
}
