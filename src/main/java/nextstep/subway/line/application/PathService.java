package nextstep.subway.line.application;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.FindPathRequest;
import nextstep.subway.line.dto.FindPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;
	private final PathFinder pathFinder;

	public PathService(SectionRepository sectionRepository,
		StationRepository stationRepository, PathFinder pathFinder) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
		this.pathFinder = pathFinder;
	}

	public FindPathResponse findShortestPath(FindPathRequest findPathRequest) {
		Sections sections = new Sections(sectionRepository.findAllByUpStationIdOrDownStationId(
			findPathRequest.getSource(), findPathRequest.getTarget()));
		List<Line> lines = sections.getLinesDistinct();
		Station sourceStation = findStationById(findPathRequest.getSource());
		Station targetStation = findStationById(findPathRequest.getTarget());

		GraphPath<Station, DefaultWeightedEdge> graphPath = pathFinder.findShortestPath(sourceStation, targetStation,
			lines);
		return FindPathResponse.of(graphPath);
	}

	private Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(Exceptions.STATION_NOT_EXIST::getException);
	}
}
