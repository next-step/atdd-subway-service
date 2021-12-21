package nextstep.subway.path.domain;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SectionEdge extends DefaultWeightedEdge {

	private final Station source;
	private final Station target;
	private final double weight;

	public SectionEdge(Station source, Station target, double weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public SectionEdge(Section section) {
		this.source = section.getUpStation();
		this.target = section.getDownStation();
		this.weight = section.getDistanceValue();
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SectionEdge that = (SectionEdge)o;
		return Double.compare(that.weight, weight) == 0 && Objects.equals(source, that.source)
			&& Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target, weight);
	}
}
