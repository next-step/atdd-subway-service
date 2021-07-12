package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum ChargePolicy {
	OVER_FIFTY(50, 8, 100) {
		@Override
		public BigDecimal getCharge(int distance) {
			return calculateOverFare(distance, this)
					.add(OVER_TEN_BY_FIFTY.getCharge(this.getMinimumApplicableDistance()));
		}
	},
	OVER_TEN_BY_FIFTY(10, 5, 100) {
		@Override
		public BigDecimal getCharge(int distance) {
			return calculateOverFare(distance, this)
					.add(BASE_CHARGE.getCharge(this.getMinimumApplicableDistance()));
		}
	},
	BASE_CHARGE(0, 0, 0) {
		@Override
		public BigDecimal getCharge(int distance) {
			return BigDecimal.valueOf(1250);
		}
	};

	private final int minimumApplicableDistance;
	private final int perDistance;
	private final int perExtraCharge;

	ChargePolicy(int minimumApplicableDistance, int perDistance, int perExtraCharge) {
		this.minimumApplicableDistance = minimumApplicableDistance;
		this.perDistance = perDistance;
		this.perExtraCharge = perExtraCharge;
	}

	public abstract BigDecimal getCharge(int distance);

	public static ChargePolicy valueMatchedByDistance(int distance) {
		return Arrays.stream(ChargePolicy.values())
				.filter(chargePolicy -> chargePolicy.filterDistance(distance))
				.findFirst()
				.orElse(BASE_CHARGE);
	}

	private boolean filterDistance(int distance) {
		return minimumApplicableDistance < distance;
	}

	private static BigDecimal calculateOverFare(int distance, ChargePolicy chargePolicy) {
		return BigDecimal.valueOf((Math.ceil((distance - chargePolicy.minimumApplicableDistance - 1) / chargePolicy.perDistance) + 1) * chargePolicy.perExtraCharge);
	}

	public int getMinimumApplicableDistance() {
		return minimumApplicableDistance;
	}
}
