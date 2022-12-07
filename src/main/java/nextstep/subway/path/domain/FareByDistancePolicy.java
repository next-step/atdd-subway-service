package nextstep.subway.path.domain;


public interface FareByDistancePolicy {
    int DEFAULT_FARE = 1_250;
    int DEFAULT_FARE_SECTION_DISTANCE = 10;
    int EXCESS_FARE_SECTION_DISTANCE = 50;
    int FARE_PER_UNIT_DISTANCE = 100;
    int CORRECTION_VALUE = 1;
    int UNIT_MID_DISTANCE = 5;
    int UNIT_LONG_DISTANCE = 8;

    int chargeFareByDistance(int distance);
}
