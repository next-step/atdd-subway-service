package nextstep.subway.path.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubwayFare {
    public static final int DEFAULT_FARE_VALUE = 1250;
    public static final SubwayFare DEFAULT_FARE = new SubwayFare(DEFAULT_FARE_VALUE);

    private static final int MAX_DISCOUNT_RATE = 100;
    private static final int MIN_DISCOUNT_RATE = 0;
    private static final int FIRST_OVER_FARE_RANGE_START = 10;
    private static final int FIRST_OVER_FARE_RANGE_END = 50;
    private static final int FIRST_OVER_FARE_RANGE_INTERVAL = 5;
    public static final int FIRST_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int SECOND_OVER_FARE_RANGE_START = 50;
    private static final int SECOND_OVER_FARE_RANGE_INTERVAL = 8;
    public static final int SECOND_OVER_FARE_RANGE_FARE_UNIT = 100;

    private static final int NO_OVER_FARE = 0;
    private static Map<Integer,SubwayFare> cacheMap = new HashMap<>();

    public static SubwayFare of(Integer value){
        if(cacheMap.containsKey(value)){
            return cacheMap.get(value);
        }
        SubwayFare newSubwayFare = new SubwayFare(value);
        cacheMap.put(value,newSubwayFare);
        return newSubwayFare;
    }

    private int value;

    //For JSON Serialize
    private SubwayFare(){
        this.value = 0;
    }

    private SubwayFare(int value) {
        if(value < 0){
            throw new IllegalArgumentException("운임은 음수가 될 수 없습니다.");
        }
        this.value = value;
    }

    public static SubwayFare calculateByDistance(int distance) {
        return new SubwayFare(DEFAULT_FARE_VALUE
                + calculateOverFareInFirstRange(distance)
                + calculateOverFareInSecondRange(distance));
    }

    private static int calculateOverFareInFirstRange(int distance) {
        int overDistance = Math.min(distance , FIRST_OVER_FARE_RANGE_END) - FIRST_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / FIRST_OVER_FARE_RANGE_INTERVAL) + 1) * FIRST_OVER_FARE_RANGE_FARE_UNIT);
    }

    private static boolean hasOverDistance(int overDistance){
        return overDistance > 0;
    }

    private static int calculateOverFareInSecondRange(int distance) {
        int overDistance = distance - SECOND_OVER_FARE_RANGE_START;
        if(!hasOverDistance(overDistance)){
            return NO_OVER_FARE;
        }
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_FARE_RANGE_INTERVAL) + 1) * SECOND_OVER_FARE_RANGE_FARE_UNIT);
    }

    public SubwayFare plus(int plusValue){
        return new SubwayFare(value + plusValue);
    }

    public SubwayFare subtract(int subtractValue){
        return new SubwayFare(value - subtractValue);
    }

    public SubwayFare discountedByPercent(int percent){
        if(percent > MAX_DISCOUNT_RATE || percent < MIN_DISCOUNT_RATE){
            throw new IllegalArgumentException("할인율은 0에서 100사이어야 합니다.");
        }
        double discountRate =  (double) percent / 100;
        return new SubwayFare((int) (value * (1 - discountRate)));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubwayFare fare = (SubwayFare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SubwayFare{" +
                "value=" + value +
                '}';
    }
}
