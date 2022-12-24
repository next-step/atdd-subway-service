package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse path(Long sourceId, Long targetId, LoginMember loginMember) {
        PathResult pathResult= pathFinder.findPath2(getStation(sourceId), getStation(targetId), sectionRepository.findAll());
        if(loginMember != null){
            return PathResultConvertor.convert(pathResult, loginMember);
        }
        return PathResultConvertor.convert(pathResult);
    }

    private Station getStation(Long sourceId) {
        return stationRepository.findById(sourceId)
            .orElseThrow(
                () -> new IllegalArgumentException("역 조회 실패")
            );
    }
}
