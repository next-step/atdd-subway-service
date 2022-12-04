package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

@Component
public class LineFarePolicy implements FarePolicy {

    @Override
    public Fare calculate(Path path) {
        Sections sections = path.getSections();

        return sections.getAllLineFare()
                .stream()
                .max(Fare::compare)
                .orElseThrow(RuntimeException::new);
    }
}
