package nextstep.subway.path.domain;

import nextstep.subway.line.domain.AdditionalFee;
import nextstep.subway.line.domain.Distance;

public class Fare {
    public static final FareStage defaultFareStage = new FareStage(0, 1250, 10);
    public static final FareStage firstFareStage = new FareStage(10, 100, 5);
    public static final FareStage secondFareStage = new FareStage(50, 800, 8);

    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare(Distance distance) {
        this.fare = defaultFareStage.getFarePerDistance() + calculateOverFare(distance);
    }

    private int calculateOverFare(Distance distance) {
        int overFare = 0;
        if (firstFareStage.in(distance)) {
            overFare += Math.min(firstFareStage.calculateOverFare(distance), calculateFirstStageMaxFare());
        }
        if (secondFareStage.in(distance)) {
            overFare += secondFareStage.calculateOverFare(distance);
        }
        return overFare;
    }

    private int calculateFirstStageMaxFare() {
        return (secondFareStage.getFromDistance() - firstFareStage.getFromDistance())
            / firstFareStage.getPerDistance() * firstFareStage.farePerDistance;
    }

    public int getValue() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Fare fare1 = (Fare)o;

        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return fare;
    }

    public Fare add(AdditionalFee additionalFee) {
        return new Fare(this.fare + additionalFee.getValue());
    }

    public static class FareStage {
        private int fromDistance;
        private int farePerDistance;
        private int perDistance;

        public FareStage(int fromDistance, int farePerDistance, int perDistance) {
            this.fromDistance = fromDistance;
            this.farePerDistance = farePerDistance;
            this.perDistance = perDistance;
        }

        public int getFromDistance() {
            return fromDistance;
        }

        public int getFarePerDistance() {
            return farePerDistance;
        }

        public int getPerDistance() {
            return perDistance;
        }

        public boolean in(Distance distance) {
            return this.fromDistance < distance.getValue();
        }

        public int calculateOverFare(Distance distance) {
            int calculatedDistance = distance.getValue() - this.fromDistance;
            return (int)((Math.ceil((calculatedDistance - 1) / this.perDistance) + 1)
                * this.farePerDistance);
        }
    }

}
