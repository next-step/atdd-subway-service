package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;

public class LineFarePolicy {
    public static int calculateExcessFare(List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("주어진 노선의 최대 노선 추가요금을 찾을 수 없습니다."));
    }
}
