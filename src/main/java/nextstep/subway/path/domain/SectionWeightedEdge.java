package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.station.domain.Station;

public class SectionWeightedEdge extends DefaultWeightedEdge {
	private Station source;
	private Station target;
	private int extraCharge;

	public SectionWeightedEdge(Station source, Station target, int extraCharge) {
		this.source = source;
		this.target = target;
		this.extraCharge = extraCharge;
	}

	@Override
	public Station getSource() {
		return source;
	}

	@Override
	public Station getTarget() {
		return target;
	}

	public int getExtraCharge() {
		return extraCharge;
	}
}
