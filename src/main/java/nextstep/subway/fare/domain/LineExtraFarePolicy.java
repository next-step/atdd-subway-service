package nextstep.subway.fare.domain;

import org.springframework.stereotype.Component;

import nextstep.subway.fare.dto.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;

@Component
public class LineExtraFarePolicy implements FarePolicy{

	@Override
	public Fare calculate(Path path) {
		return Fare.from(maxExtraFareByLine(path.getSections()));
	}

	private int maxExtraFareByLine(Sections sections) {
		return sections.getLines()
			.stream()
			.mapToInt(Line::getExtraFare)
			.max()
			.orElse(0);
	}
}
