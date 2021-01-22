package nextstep.subway.path.application.farepolicy;

public class ChargesByDistanceCalculator {
    public static final int ADDITIONAL_OVER_DISTANCE_BOUND = 50;    // 추가 운임 거리 기준 : 최대 구간
    public static final int OVER_DISTANCE_BOUND = 10;               // 추가 운임 거리 기준 : 중간 구간

    /**
     * 최단 경로의 거리 값으로 거리에 따른 추가 운임을 구합니다.
     * @param distance
     * @return
     */
    public static int getOverFareByDistance(int distance) {
        if(isOverFareDistance(distance)) {
            return calculateOverFareByDistance(distance);
        }

        if(isAdditionalOverFareDistance(distance)) {
            return calculateAdditionalOverFareDistance(distance);
        }

        return 0;
    }


    /**
     * 추가 운임 구간인 경우의 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private static int calculateAdditionalOverFareDistance(int distance) {
        return calculateOverFareByDistance(ChargesByDistanceCalculator.ADDITIONAL_OVER_DISTANCE_BOUND)
                + (int) ((Math.ceil(((distance - ChargesByDistanceCalculator.OVER_DISTANCE_BOUND) - 1) / 8) + 1) * 100);
    }

    /**
     * 추가 운임 기본 구간인 경우 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private static int calculateOverFareByDistance(int distance) {
        return (int) ((Math.ceil(((distance - ChargesByDistanceCalculator.OVER_DISTANCE_BOUND) - 1) / 5) + 1) * 100);
    }

    /**
     * 추가 운임 구간인지 반환합니다.
     * @param distance
     * @return
     */
    private static boolean isOverFareDistance(int distance) {
        return distance > ChargesByDistanceCalculator.OVER_DISTANCE_BOUND
                && distance <= ChargesByDistanceCalculator.ADDITIONAL_OVER_DISTANCE_BOUND;
    }

    /**
     * 추가 운임 기본 구간인지 반환합니다
     * @param distance
     * @return
     */
    private static boolean isAdditionalOverFareDistance(int distance) {
        return distance > ChargesByDistanceCalculator.ADDITIONAL_OVER_DISTANCE_BOUND;
    }


}
