package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Repository;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
@Repository
public class PathFinderRepositoryImpl implements PathFinderRepository{

	@Override
	public SubwayPath getDijkstraShortestPath(SubwayPathSections subwayPathSections, SubwayPathStation sourcePathStation,
		SubwayPathStation targetPathStation) {
		WeightedMultigraph<SubwayPathStation, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		DijkstraShortestPath dijkstraShortestPath = generatePathGraph(graph, subwayPathSections);

		GraphPath resultPath = dijkstraShortestPath.getPath(sourcePathStation, targetPathStation);

		List<SubwayPathStation> subwayPathStations = resultPath.getVertexList();
		int pathWeight = (int) resultPath.getWeight();
		return new SubwayPath(subwayPathStations, pathWeight);
	}

	private DijkstraShortestPath generatePathGraph(WeightedMultigraph<SubwayPathStation, DefaultWeightedEdge> graph,
		SubwayPathSections subwayPathSections) {

		addVertexTarget(graph, subwayPathSections);
		addEdgeWeightTarget(graph, subwayPathSections);

		return new DijkstraShortestPath(graph);
	}

	private void addEdgeWeightTarget(WeightedMultigraph<SubwayPathStation, DefaultWeightedEdge> graph,
		SubwayPathSections subwayPathSections) {
		subwayPathSections.getSubwayPathSections()
			.forEach(subwayPathSection -> graph.setEdgeWeight(graph.addEdge(subwayPathSection.getUpStation(),
				subwayPathSection.getDownStation()), subwayPathSection.getDistance()));
	}

	private void addVertexTarget(WeightedMultigraph<SubwayPathStation, DefaultWeightedEdge> graph,
		SubwayPathSections subwayPathSections) {
		subwayPathSections.getSubwayPathSections()
			.stream()
			.map(SubwayPathSection::getSubwayPathStations)
			.flatMap(Collection::stream)
			.map(subwayPathStation -> graph.addVertex(subwayPathStation));
	}
}
