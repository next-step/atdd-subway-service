package nextstep.subway.generator;

import static nextstep.subway.common.domain.Name.*;
import static nextstep.subway.generator.StationGenerator.*;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;

public class SectionGenerator {

	public static Section section(String upStationName, String downStationName, int distance) {
		return Section.of(station(from(upStationName)), station(from(downStationName)), Distance.from(distance));
	}
}
