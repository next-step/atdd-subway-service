package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.FindPathValidator;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathFinder {
	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;
	private final FindPathValidator findPathValidator;
	private final ShortestPathFinder shortestPathFinder;

	public PathFinder(SectionRepository sectionRepository, StationRepository stationRepository, FindPathValidator findPathValidator, ShortestPathFinder shortestPathFinder) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
		this.findPathValidator = findPathValidator;
		this.shortestPathFinder = shortestPathFinder;
	}

	public PathResponse findPath(Long sourceStationId, Long targetStationId) {
		List<Section> sections = sectionRepository.findAll();
		List<Station> stations = stationRepository.findAll();
		findPathValidator.validate(sourceStationId, targetStationId, stations);
		Station sourceStation = stations.stream().filter(station -> station.getId().equals(sourceStationId)).findFirst().get();
		Station targetStation = stations.stream().filter(station -> station.getId().equals(targetStationId)).findFirst().get();
		return shortestPathFinder.findPath(sections, stations, sourceStation, targetStation);
	}
}
