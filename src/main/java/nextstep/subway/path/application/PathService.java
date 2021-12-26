package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final StationService stationService;
	private final SectionRepository sectionRepository;

	public PathService(final StationService stationService, final SectionRepository sectionRepository) {
		this.stationService = stationService;
		this.sectionRepository = sectionRepository;
	}

	public PathResponse find(Long sourceId, Long targetId, LoginMember loggedMember) {
		Station source = stationService.findStationById(sourceId);
		Station target = stationService.findStationById(targetId);
		List<Section> sectionList = sectionRepository.findAll();
		PathFinder pathFinder = PathFinder.of(Sections.of(sectionList));
		return pathFinder.findPath(source, target, loggedMember);
	}

}
