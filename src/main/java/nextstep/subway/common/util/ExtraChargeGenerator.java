package nextstep.subway.common.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * packageName : nextstep.subway.common.util
 * fileName : ExtraChargeGenerator
 * author : haedoang
 * date : 2021/12/10
 * description :
 */
public class ExtraChargeGenerator {
    private static AtomicInteger integer;
    public static final int UNIT = 1_000;

    static {
        integer = new AtomicInteger();
    }

    public static int value() {
        return integer.get() * UNIT;
    }

    public static int generate() {
        return integer.incrementAndGet() * UNIT;
    }


}
