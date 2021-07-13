package nextstep.subway.line.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum LineSurcharge {
    SINBUNDANG("신분당선",BigDecimal.valueOf(1000)),
    YONGIND_EVERLINE("용인경전철",BigDecimal.valueOf(200)),
    UIJEONGBU("의정부경전철",BigDecimal.valueOf(300));

    private String name;
    private BigDecimal surcharge;

    LineSurcharge(String name, BigDecimal surcharge) {
        this.name = name;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public static BigDecimal price(String lineName) {
        return Arrays.stream(LineSurcharge.values())
                .filter(lineSurcharge -> lineSurcharge.name.equals(lineName))
                .findFirst()
                .map(lineSurcharge -> lineSurcharge.surcharge)
                .orElse(BigDecimal.ZERO);
    }

}
