package nextstep.subway.section.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections(){
	}

	public void addAll(Section upSection, Section downSection) {
		this.sections.add(upSection);
		this.sections.add(downSection);
	}

	public List<Section> getSections() {
		return this.sections;
	}

	public void add(Section section) {
		checkExistBoth(section);
		checkNotExistBoth(section);
		// 상행선이 있을떄
		if (isExistUpSection(section)) {
			updateUpSection(section);
		}
		// 하행선이 있을때
		if (isExistDownSection(section)) {
			updateDownSection(section);
		}

		this.sections.add(section);
	}

	private void checkExistBoth(Section section) {
		if (isExistUpStation(section) && isExistDownStation(section)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	private void checkNotExistBoth(Section section) {
		if (isNotExistUpStation(section) && isNotExistDownStation(section)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private boolean isNotExistDownStation(Section newSection) {
		return sections.stream().noneMatch(
			section -> section.getDownStation() == newSection.getDownStation()
				|| section.getDownStation() == newSection.getUpStation()
		);
	}

	private boolean isNotExistUpStation(Section newSection) {
		return sections.stream().noneMatch(
			section -> section.getUpStation() == newSection.getUpStation()
				|| section.getUpStation() == newSection.getDownStation()
		);
	}

	private boolean isExistDownStation(Section newSection) {
		return sections.stream().anyMatch(section -> section.getDownStation() == newSection.getDownStation());
	}

	private boolean isExistUpStation(Section newSection) {
		return sections.stream().anyMatch(section -> section.getUpStation() == newSection.getUpStation());
	}

	private boolean isExistUpSection(Section newSection) {
		return sections.stream().anyMatch(section -> section.getUpStation() == newSection.getUpStation());
	}

	private boolean isExistDownSection(Section newSection) {
		return sections.stream().anyMatch(section -> section.getDownStation() == newSection.getDownStation());
	}

	private void updateUpSection(Section newSection) {
		this.sections.stream()
			.filter(oldSection -> newSection.getUpStation() == oldSection.getUpStation())
			.findFirst()
			.ifPresent(oldSection -> addUpSection(newSection, oldSection));
	}

	private void updateDownSection(Section newSection) {
		this.sections.stream()
			.filter(oldSection -> newSection.getDownStation() == oldSection.getDownStation())
			.findFirst()
			.ifPresent(oldSection -> addDownSection(newSection, oldSection));
	}

	private void addDownSection(Section newSection, Section oldSection) {
		if (oldSection.getUpStation() == null) {
			oldSection.updateDownStation(newSection.getUpStation());
			return;
		}

		int distance = oldSection.getSubtractDistance(newSection);
		sections.add(
			new Section(newSection.getLine(), newSection.getDownStation(), oldSection.getDownStation(), distance));
		sections.remove(oldSection);
	}

	private void addUpSection(Section newSection, Section oldSection) {
		int distance = oldSection.getSubtractDistance(newSection);
		sections.add(
			new Section(newSection.getLine(), newSection.getDownStation(), oldSection.getDownStation(), distance));
		sections.remove(oldSection);
	}
}
