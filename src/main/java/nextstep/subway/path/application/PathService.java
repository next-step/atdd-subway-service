package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
public class PathService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final FareCalculator fareCalculator;

    public PathService(MemberRepository memberRepository, StationRepository stationRepository, SectionRepository sectionRepository, FareCalculator fareCalculator) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.fareCalculator = fareCalculator;
    }


    @Transactional(readOnly = true)
    public PathResponse findPaths(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId).orElseThrow(NoResultException::new);
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(NoResultException::new);
        Sections sections = new Sections(sectionRepository.findAll());

        Path path = new PathFinder().find(sections, sourceStation, targetStation);

        int fare = fareCalculator.calculate(path, loginMember);

        return PathResponse.of(path, fare);
    }
}
