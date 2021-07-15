package nextstep.subway.common.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum SurchargeByLine {
    SINBUNDANG("신분당선",BigDecimal.valueOf(1000)),
    YONGIND_EVERLINE("용인경전철",BigDecimal.valueOf(200)),
    UIJEONGBU("의정부경전철",BigDecimal.valueOf(300)),
    NORMAL("일반",BigDecimal.ZERO);


    private String name;
    private BigDecimal surcharge;

    SurchargeByLine(String name, BigDecimal surcharge) {
        this.name = name;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public BigDecimal charge() {
        return surcharge;
    }





}
