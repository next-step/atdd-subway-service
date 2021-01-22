package nextstep.subway.path.domain.farePolicy;

/**
 * @author : byungkyu
 * @date : 2021/01/22
 * @description :
 **/
public class MiddleDistanceFarePolicy implements DistanceFarePolicy{
	private static int TEN_KILOMETER_DISTANCE = 10;
	private static int FIFTY_KILOMETER_DISTANCE = 50;
	private static int STANDARD = 5;

	private int distance;

	public MiddleDistanceFarePolicy(int distance) {
		this.distance = distance;
	}

	@Override
	public int calculate() {
		return FarePolicy.calculateOverFare(distance - TEN_KILOMETER_DISTANCE, STANDARD);
	}

	public static boolean isMiddleDistance(int distance){
		return distance > TEN_KILOMETER_DISTANCE && distance <= FIFTY_KILOMETER_DISTANCE;
	}
}
