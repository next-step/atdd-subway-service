package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;

@Embeddable
@Access(AccessType.FIELD)
public class SubwayFare {
	public static final BigDecimal SUBWAY_BASE_FARE = new BigDecimal(1250);

	private final BigDecimal subwayFare;

	protected SubwayFare() {
		subwayFare = SUBWAY_BASE_FARE;
	}

	private SubwayFare(BigDecimal subwayFare) {
		this.subwayFare = subwayFare;
	}

	public static SubwayFare of(BigDecimal subwayFare) {
		return new SubwayFare(subwayFare);
	}

	public int value() {
		return subwayFare.intValue();
	}

	public SubwayFare calculateDistanceOverFare(Distance distance) {
		SubwayFare calculatedOverFare = Arrays.stream(OverFare.values())
			.filter(overFare -> overFare.checkGrade(distance))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("거리 값은 0이상이어야 합니다."))
			.calculate(distance);
		return this.add(calculatedOverFare);
	}

	private SubwayFare add(BigDecimal overFare) {
		return new SubwayFare(this.subwayFare.add(overFare));
	}

	private SubwayFare add(SubwayFare overFare) {
		return new SubwayFare(this.subwayFare.add(overFare.subwayFare));
	}

	public SubwayFare calculateLineOverFare(List<Section> allSections, List<SectionEdge> pathEdges) {
		int maxOverFare = allSections.stream()
			.filter(section -> pathEdges.contains(new SectionEdge(section)))
			.mapToInt(section -> section.getLine().getOverFare())
			.max()
			.orElseThrow(() -> new IllegalArgumentException("최단경로 구간이 존재하지 않습니다."));
		return this.add(new BigDecimal(maxOverFare));
	}

	public SubwayFare calculateDiscountFareByAge(LoginMember loginMember) {
		return Arrays.stream(Discount.values())
			.filter(discount -> discount.checkGrade(loginMember))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("나이는 0보다 커야합니다."))
			.discount(this);
	}

	public BigDecimal subtract(SubwayFare subwayFare) {
		return this.subwayFare.subtract(subwayFare.subwayFare);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SubwayFare that = (SubwayFare)o;
		return Objects.equals(subwayFare, that.subwayFare);
	}

	@Override
	public int hashCode() {
		return Objects.hash(subwayFare);
	}
}
