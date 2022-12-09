package nextstep.subway.line.application;

import static nextstep.subway.line.application.PathResultConvertor.*;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.PathGraph;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathGraph pathGraph;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository, PathGraph pathGraph) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.pathGraph = pathGraph;
    }

    public PathResponse path(Long sourceId, Long targetId, LoginMember loginMember) {
        PathResult pathResult= pathGraph.findPath(getStation(sourceId), getStation(targetId), sectionRepository.findAll());
        if(isLoggedIn(loginMember)){
            return convert(pathResult, loginMember);
        }
        return convert(pathResult);
    }

    private boolean isLoggedIn(LoginMember loginMember) {
        return loginMember != null;
    }

    private Station getStation(Long sourceId) {
        return stationRepository.findById(sourceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("역 조회 실패")
                );
    }
}
