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
		SectionEdge sectionEdge = new SectionEdge(section);
		super.addEdge(section.getUpStation(), section.getDownStation(),sectionEdge);
		super.setEdgeWeight(sectionEdge, section.getDistanceValue());
	}
}
