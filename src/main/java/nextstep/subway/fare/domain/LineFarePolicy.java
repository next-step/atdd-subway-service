package nextstep.subway.fare.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Component;

@Component
public class LineFarePolicy {
    public Fare calculate(List<Line> lines) {
        return lines.stream()
                .map(Line::getExtraFare)
                .max(Fare::compareTo)
                .orElseThrow(RuntimeException::new);
    }
}
