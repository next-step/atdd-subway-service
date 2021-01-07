package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

public class TotalLines {
	private List<Line> lines;

	protected TotalLines() {
	}

	public TotalLines(List<Line> lines) {
		this.lines = lines;
	}

	public List<Section> allSections() {
		List<Section> sections = new ArrayList<>();
		for(Line line : lines) {
			sections.addAll(line.getSections());
		}
		return sections;
	}
}
