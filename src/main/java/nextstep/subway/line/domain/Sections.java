package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

@Embeddable
public class Sections {

	private static final String INVALID_SECTION_ERROR_MESSAGE = "지하철 구간은 반드시 존재해야 합니다.";
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	private Sections(List<Section> sections) {
		Assert.notNull(sections, INVALID_SECTION_ERROR_MESSAGE);
		this.sections.addAll(sections);
	}

	public static Sections from(Section section) {
		Assert.notNull(section, INVALID_SECTION_ERROR_MESSAGE);
		return new Sections(Collections.singletonList(section));
	}

	public static Sections from(List<Section> sections) {
		return new Sections(sections);
	}

	public List<Section> getList() {
		return sections;
	}

	public void setLine(Line line) {
		this.sections.forEach(section -> section.updateLine(line));
	}
}
