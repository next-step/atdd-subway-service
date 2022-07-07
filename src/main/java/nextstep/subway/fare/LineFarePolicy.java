package nextstep.subway.fare;

import nextstep.subway.line.domain.Lines;
import org.springframework.stereotype.Component;

@Component
public class LineFarePolicy {
    public Fare calculate(Lines lines) {
        return lines.getFare();
    }
}
