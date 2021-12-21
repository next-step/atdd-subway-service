package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayFare;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

	private final StationService stationService;
	private final LineService lineService;

	public PathService(StationService stationService, LineService lineService) {
		this.stationService = stationService;
		this.lineService = lineService;
	}

	public PathResponse findPaths(PathRequest pathRequest, LoginMember loginMember) {
		validateStartStationEqualsEndStation(pathRequest);
		validateNoneStation(pathRequest);

		Station sourceStation = stationService.findById((long)pathRequest.getSourceId());
		Station targetStation = stationService.findById((long)pathRequest.getTargetId());

		SectionEdge pathFinderParam = new SectionEdge(sourceStation, targetStation, 0);
		PathFinderResponse pathFinderResponse = createPathFinderResponse(pathFinderParam, loginMember);
		return PathResponse.of(pathFinderResponse);
	}

	private PathFinderResponse createPathFinderResponse(SectionEdge pathFinderParam, LoginMember loginMember) {
		List<Section> allSections = findAllSections();
		PathFinder pathFinder = PathFinder.create(stationService.findAll(), allSections);
		List<StationResponse> stationResponses = extractStationResponses(pathFinder.findShortestPathVertexes(pathFinderParam));
		List<SectionEdge> shortestPathEdges = pathFinder.findShortestPathEdges(pathFinderParam);
		Distance distance = pathFinder.findShortestPathDistance(pathFinderParam);
		SubwayFare subwayFare = SubwayFare.of(SubwayFare.SUBWAY_BASE_FARE)
			.calculateLineOverFare(allSections, shortestPathEdges)
			.calculateDistanceOverFare(distance)
			.calculateDiscountFareByAge(loginMember);
		return new PathFinderResponse(stationResponses, shortestPathEdges, distance, subwayFare);

	}

	private List<Section> findAllSections() {
		List<Section> allSections = new ArrayList<>();
		lineService.findAll().stream()
			.forEach(line -> allSections.addAll(line.getSections()));
		return allSections;
	}

	private void validateNoneStation(PathRequest pathRequest) {
		if (!isExisted(pathRequest)) {
			throw new IllegalArgumentException("출발역과 도착역 모두 존재해야 합니다.");
		}
	}

	private boolean isExisted(PathRequest pathRequest) {
		return stationService.findAll()
			.stream()
			.map(Station::getId)
			.collect(Collectors.toList())
			.containsAll(Lists.newArrayList(
				(long)pathRequest.getSourceId(), (long)pathRequest.getTargetId()));
	}

	private void validateStartStationEqualsEndStation(PathRequest pathRequest) {
		if (pathRequest.getSourceId() == pathRequest.getTargetId()) {
			throw new IllegalArgumentException("출발역과 도착역이 동일하면 안 됩니다.");
		}
	}

	private List<StationResponse> extractStationResponses(List<Station> pathVertexes) {
		return pathVertexes.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
