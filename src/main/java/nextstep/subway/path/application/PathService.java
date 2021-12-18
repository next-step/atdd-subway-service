package nextstep.subway.path.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SubwayFare;
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

	public PathResponse findPaths(int source, int target) {
		validateStartStationEqualsEndStation(source, target);
		validateNoneStation(source, target);
		Station sourceStation = stationService.findById((long)source);
		Station targetStation = stationService.findById((long)target);
		PathFinder pathFinder = PathFinder.create(stationService.findAll(), findAllSections());
		List<StationResponse> stationResponses
			= extractStationResponses(pathFinder.findShortestPathVertexes(sourceStation, targetStation));
		Distance distance = pathFinder.findShortestPathDistance(sourceStation, targetStation);
		SubwayFare subwayFare = SubwayFare.calculate(distance);
		return PathResponse.of(stationResponses, distance, subwayFare);
	}

	private List<Section> findAllSections() {
		List<Section> allSections = new ArrayList<>();
		lineService.findAll().stream()
			.forEach(line -> allSections.addAll(line.getSections()));
		return allSections;
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

	private List<StationResponse> extractStationResponses(List<Station> pathVertexes) {
		return pathVertexes.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
