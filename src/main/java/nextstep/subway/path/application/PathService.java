package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

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
		validateStartStationEqualsEndStation(source, target);
		validateNoneStation(source, target);
		PathFinder pathFinder = PathFinder.create(stationService.findAll(), sectionService.findAll());
		List<StationResponse> stationResponses = extractStationResponses(
			pathFinder.findShortestPathVertexes(source, target));
		Distance distance = pathFinder.findShortestPathDistance(source, target);
		return PathResponse.of(stationResponses, distance);
	}

	private void validateNoneStation(int source, int target) {
		if (!isExisted(source, target)) {
			throw new IllegalArgumentException("출발역과 도착역 모두 존재해야 합니다.");
		}
	}

	private boolean isExisted(int source, int target) {
		return stationService.findAll()
			.stream()
			.map(Station::getId)
			.collect(Collectors.toList())
			.containsAll(Lists.newArrayList((long)source, (long)target));
	}

	private void validateStartStationEqualsEndStation(int source, int target) {
		if (source == target) {
			throw new IllegalArgumentException("출발역과 도착역이 동일하면 안 됩니다.");
		}
	}

	private List<StationResponse> extractStationResponses(List<Long> pathVertexes) {
		return pathVertexes.stream()
			.map(stationService::findById)
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
