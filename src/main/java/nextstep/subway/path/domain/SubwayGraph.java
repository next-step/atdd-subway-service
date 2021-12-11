package nextstep.subway.path.domain;

import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {

	public SubwayGraph(
		Class<SectionEdge> ef) {
		super(ef);
	}

	public void addSectionEdge(Section section) {
		super.setEdgeWeight(super.addEdge(section.getUpStation(), section.getDownStation()), section.getDistanceValue());
	}
}
