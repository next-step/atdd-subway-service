package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.FindPathRequest;
import nextstep.subway.line.dto.FindPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final SectionRepository sectionRepository;
	private final StationRepository stationRepository;

	public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
		this.sectionRepository = sectionRepository;
		this.stationRepository = stationRepository;
	}

	public FindPathResponse findShortestPath(FindPathRequest findPathRequest) {
		Sections sections = new Sections(sectionRepository.findAllByUpStationIdOrDownStationId(
			findPathRequest.getSource(), findPathRequest.getTarget()));

		List<Line> lines = sections.getLinesDistinct();

		Station sourceStation = findStationById(findPathRequest.getSource());
		Station targetStation = findStationById(findPathRequest.getTarget());

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		for (Line line : lines) {
			for (Section section : line.getSections()) {
				graph.addVertex(section.getUpStation());
				graph.addVertex(section.getDownStation());
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
					section.getDistance());
			}
		}

		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
		List<StationResponse> shortestPath = path.getVertexList()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
		int distance = (int)path.getWeight();
		return new FindPathResponse(shortestPath, distance);
	}

	private Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(Exceptions.STATION_NOT_EXIST::getException);
	}
}
