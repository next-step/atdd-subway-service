package nextstep.subway.path.domain.farePolicy;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public class LongDistanceFarePolicy implements DistanceFarePolicy{
	private static int FIFTY_KILOMETER_DISTANCE = 50;
	private static int FOURTY_KILOMETER_DISTANCE = 40;

	private static int STANDARD_PER_FIVE_KILOMETER = 5;
	private static int STANDARD_PER_EIGHT_KILOMETER = 8;

	private int distance;

	public LongDistanceFarePolicy(int distance) {
		this.distance = distance;
	}

	@Override
	public int calculate() {
		return middleDistanceFare() + longDistanceFare();
	}

	private int longDistanceFare() {
		return FarePolicy.calculateOverFare((distance - FIFTY_KILOMETER_DISTANCE), STANDARD_PER_EIGHT_KILOMETER);
	}

	private int middleDistanceFare() {
		return FarePolicy.calculateOverFare(FOURTY_KILOMETER_DISTANCE, STANDARD_PER_FIVE_KILOMETER);
	}

	public static boolean isLongDistance(int distance){
		return distance > FIFTY_KILOMETER_DISTANCE;
	}
}
