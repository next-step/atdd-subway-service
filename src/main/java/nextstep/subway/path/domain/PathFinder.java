package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;

public class PathFinder {

	private PathFinder() {

	}

	public static PathFinder of(List<Line> lines) {
		return new PathFinder();
	}
}
