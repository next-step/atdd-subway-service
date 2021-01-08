package nextstep.subway.fare.domain;

import nextstep.subway.fare.dto.FareRequest;
import nextstep.subway.fare.dto.FareResponse;

public class Fare {

    private final FareRequest fareRequest;

    public Fare(FareRequest fareRequest) {
        this.fareRequest = fareRequest;
    }

    public int getTotalFare() {
        FareLine fareLine = new FareLine(fareRequest.getLines(), fareRequest.getPathStations());
        FareDistance fareDistance = new FareDistance(fareRequest.getDistance());
        int amount = new OverCharges()
                .addCharge(fareLine)
                .addCharge(fareDistance)
                .getAmount();

        if (fareRequest.hasAge()) {
            amount -= getAgeDiscount(amount);
        }

        return amount;
    }

    public static FareResponse ofResponse(FareRequest request) {
        int totalFare = new Fare(request).getTotalFare();
        return new FareResponse(totalFare);
    }

    private int getAgeDiscount(int amount) {
        return new FareAge(fareRequest.getAge()).getAmount(amount);
    }
}
