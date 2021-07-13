package nextstep.subway.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class AdditionalFareEdge extends DefaultWeightedEdge {
	private int additionalFare;

	public AdditionalFareEdge(int additionalFare) {
		this.additionalFare = additionalFare;
	}

	public int getAdditionalFare() {
		return additionalFare;
	}
}
