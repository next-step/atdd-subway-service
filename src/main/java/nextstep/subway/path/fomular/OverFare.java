package nextstep.subway.path.fomular;

public enum OverFare {
    DISTANCE_10_KM(10, 5),
    DISTANCE_50_KM(50, 8);

    int overDistance;
    int discountPer;

    OverFare(int overDistance, int discountPer) {
        this.overDistance = overDistance;
        this.discountPer = discountPer;
    }

    public int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - this.overDistance - 1) / this.discountPer) + 1) * 100);
    }
}
