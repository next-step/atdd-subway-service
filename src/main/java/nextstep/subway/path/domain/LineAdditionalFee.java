package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.Arrays;
import java.util.Set;

public enum LineAdditionalFee {

    LINE_TWO(1L, 100),
    LINE_THREE(2L, 200),
    LINE_SINBUNDANG(3L, 300),
    LINE_BUNDANG(4L, 400);

    private Long id;
    private int additionalFee;

    LineAdditionalFee(Long id, int additionalFee) {
        this.id = id;
        this.additionalFee = additionalFee;
    }

    public int maximumAdditionalFee(Set<Long> lineIds) {
        return Arrays.stream(values())
                .filter(lineAdditionalFee -> lineIds.contains(lineAdditionalFee.getId()))
                .mapToInt(LineAdditionalFee::getAdditionalFee)
                .max()
                .orElse(0);
    }

    public Long getId() {
        return id;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }
}
