package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nextstep.subway.station.domain.Station;

public class LineSections {
	List<SectionNew> sections = new ArrayList<>();

	public LineSections(List<SectionNew> sections) {
		this.sections.addAll(sections);
	}

	public void add(SectionNew section) {
		this.sections.add(section);
	}

	public List<SectionNew> getSections() {
		return this.sections;
	}

	public boolean isEmpty() {
		return this.sections.isEmpty();
	}

	public Optional<SectionNew> findByUpStation(Station upStation) {
		return this.sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst();
	}

	public Optional<SectionNew> findByDownStation(Station downStation) {
		return this.sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst();
	}

	public Station findFirstUpstation() {
		return this.sections.get(0).getUpStation();
	}
}
