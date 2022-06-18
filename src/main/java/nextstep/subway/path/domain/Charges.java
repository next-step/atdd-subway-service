package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

public class Charges {
    private final List<Charge> charges;

    private Charges(List<Charge> charges) {
        this.charges = charges;
    }

    public static Charges of(Charge... charges) {
        return new Charges(Arrays.asList(charges));
    }

    public Charge sum() {
        return charges.stream()
                .reduce(Charge::add)
                .orElse(Charge.ZERO);
    }
}
