package nextstep.subway.utils;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class StationPathCalculator {
	public static PathResponse findShortestDistance(List<Line> lines, Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, Section> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		// todo : 심각하다
		for(Line line : lines) {
			for (Station station : line.getStations()) {
				graph.addVertex(station);
			}
			for (Section section : line.getSections().getSections()) {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().value());
			}
		}
		List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(sourceStation, targetStation);

		List<Station> stations = new ArrayList<>();
		
		//todo : Int로 해라
		double weight = 0;
		for(GraphPath path : paths) {
			stations = (List<Station>)path.getEdgeList();
			weight = path.getWeight();
		}

		return new PathResponse(StationResponse.of(stations), weight);
	}
}
