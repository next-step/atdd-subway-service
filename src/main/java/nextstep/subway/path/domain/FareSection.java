package nextstep.subway.path.domain;

public class FareSection {

    private int lowerLimit;
    private int upperLimit;
    private int perKilo;
    private int perFare;

    public FareSection(int lowerLimit, int upperLimit, int perKilo, int perFare) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.perKilo = perKilo;
        this.perFare = perFare;
    }

    public int getFare(int distance) {
        if (distance <= lowerLimit) {
            return 0;
        }

        if (distance > upperLimit) {
            return (int) ((Math.ceil(((upperLimit - lowerLimit) - 1) / perKilo) + 1) * perFare);
        }

        return (int) ((Math.ceil(((distance - lowerLimit) - 1) / perKilo) + 1) * perFare);
    }
}
