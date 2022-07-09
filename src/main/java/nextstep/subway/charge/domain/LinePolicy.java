package nextstep.subway.charge.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;

public class LinePolicy {
    private static final String ERROR_MESSAGE_SECTIONS_EMPTY = "노선 추가 운임을 확인할 구간 목록이 비었습니다.";
    private static final String ERROR_MESSAGE_MAX_EXTRA_CHARGE_LESS_THAN_ZERO = "최고 노선 추가 운임료가 0 미만입니다.";

    private final int extraCharge;

    public LinePolicy(List<Section> sections) {
        int maxExtraCharge = sections.stream()
                .mapToInt(Section::getExtraCharge)
                .max()
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_SECTIONS_EMPTY));

        validateExtraCharge(maxExtraCharge);
        this.extraCharge = maxExtraCharge;
    }

    private void validateExtraCharge(int maxExtraCharge) {
        if (maxExtraCharge < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_MAX_EXTRA_CHARGE_LESS_THAN_ZERO);
        }
    }

    public void applyPolicy(Charge charge) {
        charge.plus(extraCharge);
    }

}
