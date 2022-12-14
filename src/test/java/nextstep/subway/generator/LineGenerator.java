package nextstep.subway.generator;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class LineGenerator {

	public static Line line(String name, String color, String upStationName, String downStationName, int distance) {
		return Line.of(
			Name.from(name), Color.from(color),
			Sections.from(Section.of(
				Station.from(Name.from(upStationName)),
				Station.from(Name.from(downStationName)),
				Distance.from(distance)))
		);
	}

	public static Line line(String name, String color, String upStationName, String downStationName, int distance,
		int extraFare) {
		return Line.of(
			Name.from(name), Color.from(color),
			Sections.from(Section.of(
				Station.from(Name.from(upStationName)),
				Station.from(Name.from(downStationName)),
				Distance.from(distance))),
			Fare.from(extraFare)
		);
	}
}
