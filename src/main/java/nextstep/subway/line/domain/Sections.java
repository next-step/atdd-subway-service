package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	protected Sections() {
	}

	public void add(Section section) {
		sections.add(section);
	}

	public List<Station> getOrderedStations() {
		Station downStation = findUpStation();
		List<Station> stations = new ArrayList<>();

		while (downStation != null) {
			stations.add(downStation);
			Optional<Section> nextLineSection = findNextSection(downStation);
			downStation = nextLineSection.map(Section::getDownStation).orElse(null);
		}
		return stations;
	}

	private Station findUpStation() {
		Station searchStation = sections.get(0).getUpStation();
		Station upStation = searchStation;

		while (searchStation != null) {
			upStation = searchStation;
			Optional<Section> prevLineSection = findPrevSection(searchStation);
			searchStation = prevLineSection.map(Section::getUpStation).orElse(null);
		}
		return upStation;
	}

	private Optional<Section> findNextSection(Station station) {
		return sections.stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst();
	}

	private Optional<Section> findPrevSection(Station station) {
		return sections.stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Sections sections1 = (Sections)o;
		return Objects.equals(sections, sections1.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sections);
	}

	public List<Section> get() {
		return sections;
	}

	public void remove(Section section) {
		sections.remove(section);
	}
}
