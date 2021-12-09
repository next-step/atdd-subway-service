package nextstep.subway.line.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionGraph;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.policy.AgeType;
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

    public PathResponse findPathsBySection(LoginMember loginMember, PathRequest request) {
        Sections sections = new Sections(sectionRepository.findAll());
        Station sourceStation = stationRepository.findByIdElseThrow(request.getSource());
        Station targetStation = stationRepository.findByIdElseThrow(request.getTarget());
        SectionGraph graph = sections.generatePaths(sourceStation, targetStation);

        if (loginMember.isGuest()) {
            return new PathResponse(graph, sections.selectMaxSurcharge(graph));
        }

        return new PathResponse(graph, sections.selectMaxSurcharge(graph), AgeType.of(loginMember.getAge()));
    }
}

