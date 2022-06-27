package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LineFarePolicy {
    public Fare calculate(List<Line> lines) {
        return lines.stream()
                .map(Line::getSurcharge)
                .max(Fare::compareTo)
                .orElseThrow(RuntimeException::new);
    }
}
