package nextstep.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.FindPathRequest;
import nextstep.subway.path.dto.FindPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;
	private final PathFinder pathFinder;

	public PathService(StationRepository stationRepository, SectionRepository sectionRepository,
		PathFinder pathFinder) {
		this.stationRepository = stationRepository;
		this.sectionRepository = sectionRepository;
		this.pathFinder = pathFinder;
	}

	public FindPathResponse findShortestPath(FindPathRequest findPathRequest) {
		Sections sections = new Sections(sectionRepository.findDistinctBySection(findPathRequest.getSource(),
			findPathRequest.getTarget()));
		Station sourceStation = findStationById(findPathRequest.getSource());
		Station targetStation = findStationById(findPathRequest.getTarget());

		GraphPath<Station, DefaultWeightedEdge> graphPath = pathFinder.findShortestPath(sourceStation, targetStation,
			sections);
		return FindPathResponse.of(graphPath);
	}

	private Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(Exceptions.STATION_NOT_EXIST::getException);
	}
}
