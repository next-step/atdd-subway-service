package nextstep.subway.path.domain;

import org.jgrapht.GraphPath;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FareCalculator {

	public int calculateFare(GraphPath<Station, SectionWeightedEdge> path, Member member) {
		int distance = (int)path.getWeight();

		int fare = DistanceFare.findByDistance(distance).calculateFare(distance);
		int lineExtraCharge = this.getLineExtraCharge(path);
		int totalFare = fare + lineExtraCharge;

		return AgeDiscount.findByAge(member.getAge()).getDiscountedFare(totalFare);
	}

	private int getLineExtraCharge(GraphPath<Station, SectionWeightedEdge> path) {
		return path.getEdgeList().stream().map(SectionWeightedEdge::getExtraCharge)
			.max(Integer::compareTo)
			.orElseThrow(() -> new IllegalArgumentException("line 추가요금 조회시 오류 발생"));
	}
}
