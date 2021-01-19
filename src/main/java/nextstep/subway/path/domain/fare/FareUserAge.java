package nextstep.subway.path.domain.fare;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FareUserAge {
	BABY(0, 6, Money.of(0), 0),
	CHILD(6, 13, Money.of(450), 0.5),
	STUDENT(13, 18, Money.of(720), 0.8),
	ADULT(18, 65, Money.of(1250), 1),
	OLDSTER(65, Integer.MAX_VALUE, Money.of(0), 0);

	private final int minimumAge;
	private final int maximumAge;
	private final Money basicFare;
	private final double discount;

	public static FareUserAge findFareAge(Integer age) {
		if (age == null) {
			return ADULT;
		}
		return Arrays.stream(values())
			.filter(it -> it.minimumAge <= age && age < it.maximumAge)
			.findFirst()
			.orElse(ADULT);
	}

	public Money discount(Money calculateDistance) {
		return calculateDistance.multi(this.discount);
	}
}
