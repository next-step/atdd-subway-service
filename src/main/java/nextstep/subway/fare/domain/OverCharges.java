package nextstep.subway.fare.domain;

import java.util.ArrayList;
import java.util.List;

public class OverCharges extends OverCharge {
    private final List<OverCharge> overCharges = new ArrayList<>();

    public OverCharges addCharge(OverCharge charge) {
        overCharges.add(charge);
        return this;
    }

    @Override
    public int getAmount() {
        return overCharges.stream()
                .map(OverCharge::getAmount)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
