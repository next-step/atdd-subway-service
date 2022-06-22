package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

public class Surcharges {
    private final List<Surcharge> surcharges;

    private Surcharges(List<Surcharge> surcharges) {
        this.surcharges = surcharges;
    }

    public static Surcharges of(Surcharge... surcharges) {
        return new Surcharges(Arrays.asList(surcharges));
    }

    public Surcharge sum() {
        return surcharges.stream()
                .reduce(Surcharge::add)
                .orElse(Surcharge.ZERO);
    }
}
