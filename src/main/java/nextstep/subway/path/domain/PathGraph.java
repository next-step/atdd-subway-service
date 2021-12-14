package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathGraph {

	private WeightedMultigraph<Station, DefaultWeightedEdge> graph
		= new WeightedMultigraph(DefaultWeightedEdge.class);

	public void addSection(Section section) {
		this.graph.addVertex(section.getUpStation());
		this.graph.addVertex(section.getDownStation());
		this.graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()),
			section.getDistance().toInt());
	}

}
