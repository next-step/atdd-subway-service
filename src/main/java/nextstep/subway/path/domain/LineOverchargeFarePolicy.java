package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;

public interface LineOverchargeFarePolicy {
	int overcharge(int fare, List<Line> lines);
}
