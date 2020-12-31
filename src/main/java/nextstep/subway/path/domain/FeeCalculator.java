package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.dto.LineResponse;

public class FeeCalculator {
	public static final int DEDUCTION_FEE = 350;
	public static final int BASIC_FEE = 1250;
	public static final int BASIC_DISTANCE = 10;
	public static final int EXTRA_DISTANCE = 50;
	private int distance;
	private AgeGroup ageGroup;
	private Integer extraFee;

	public FeeCalculator(int distance, Integer age, List<LineResponse> lines) {
		this.distance = distance;
		this.ageGroup = AgeGroup.findAgeGroup(age);
		this.extraFee = findMaxExtraFee(lines);
	}

	public int calculate() {
		int result = BASIC_FEE;
		result += calculateOver10Fare();
		result += calculateOver50Fare();
		result += extraFee;
		return ageGroup.discountFee(result);
	}

	private int calculateOver10Fare() {
		if (distance <= BASIC_DISTANCE) {
			return 0;
		}
		int overDistance = distance - BASIC_DISTANCE;
		if (distance > EXTRA_DISTANCE) {
			overDistance = EXTRA_DISTANCE - BASIC_DISTANCE;
		}
		return (int)((Math.ceil((overDistance) / (double)5)) * 100);
	}

	private int calculateOver50Fare() {
		if (distance <= EXTRA_DISTANCE) {
			return 0;
		}
		int overDistance = distance - EXTRA_DISTANCE;
		return (int)((Math.ceil((overDistance) / (double)8)) * 100);
	}

	private int findMaxExtraFee(List<LineResponse> lines) {
		return lines.stream()
			.mapToInt(LineResponse::getExtraFee)
			.max()
			.orElse(0);
	}

}
