package nextstep.subway.utils;

import nextstep.subway.fare.domain.Fare;

public class FareCalculator {

	private static final int DISTANCE_ONE_KM_FARE = 100;

	public static Fare getSubwayFare(int distance, int extraLineFare, int age) {

		int fare = 1250;
		if (distance > 10 && distance <= 50) {
			fare += (int)((Math.ceil((distance - 1) / 5) + 1) * DISTANCE_ONE_KM_FARE);
		} else if (distance > 50) {
			fare += (int)((Math.ceil((distance - 1) / 8) + 1) * DISTANCE_ONE_KM_FARE);
		}

		fare += extraLineFare;

		if(age >= 6 && age < 13) {
		    fare = fare - 350;
		    fare = (int)(fare - Math.ceil(fare * 0.5));
		}

		if(age >= 13 && age < 19) {
			fare = fare - 350;
			fare = (int)(fare - Math.ceil(fare * 0.2));
		}

		return new Fare(fare);

	}
}
