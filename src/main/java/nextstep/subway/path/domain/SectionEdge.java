package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {
	private Section section;

	private SectionEdge() {

	}

	public static SectionEdge of(Section section) {
		SectionEdge sectionEdge = new SectionEdge();
		sectionEdge.section = section;
		return sectionEdge;
	}

	public static List<Section> toSections(List<SectionEdge> sectionEdges) {
		return sectionEdges.stream()
			.map(SectionEdge::getSection)
			.collect(Collectors.toList());
	}

	public Section getSection() {
		return section;
	}

	@Override
	public Station getSource() {
		return section.getUpStation();
	}

	@Override
	public Station getTarget() {
		return section.getDownStation();
	}

	@Override
	public double getWeight() {
		return section.getDistance();
	}
}
