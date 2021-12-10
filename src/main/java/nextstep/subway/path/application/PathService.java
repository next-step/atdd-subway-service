package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

	private final StationService stationService;
	private final SectionService sectionService;

	public PathService(StationService stationService, SectionService sectionService) {
		this.stationService = stationService;
		this.sectionService = sectionService;
	}

	public PathResponse findPaths(int source, int target) {
		PathFinder pathFinder = PathFinder.create(stationService.findAll(), sectionService.findAll());
		List<StationResponse> stationResponses = pathFinder.findShortestPathVertexes(source, target)
			.stream()
			.map(stationService::findById)
			.map(StationResponse::of)
			.collect(Collectors.toList());
		Distance distance = pathFinder.findShortestPathDistance(source, target);
		return PathResponse.of(stationResponses, distance);
	}
}
