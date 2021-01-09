package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

public class SubwayMap {
	private List<Line> lines;

	protected SubwayMap() {
	}

	public SubwayMap(List<Line> lines) {
		this.lines = lines;
	}

	public List<Section> allSections() {
		List<Section> sections = new ArrayList<>();
		lines.forEach(line -> sections.addAll(line.getSections()));
		return sections;
	}
}
