package nextstep.subway.path.dto;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.math.BigDecimal;

public class SectionEdge extends DefaultWeightedEdge {
	private BigDecimal extraCharge;

	public SectionEdge setExtraCharge(BigDecimal extraCharge) {
		this.extraCharge = extraCharge;
		return this;
	}

	public BigDecimal getExtraCharge() {
		return extraCharge;
	}
}
