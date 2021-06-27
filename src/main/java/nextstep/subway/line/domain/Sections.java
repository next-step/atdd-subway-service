package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSections() {
		return sections;
	}

	public void add(Section section) {
		sections.add(section);
	}

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		Station upStation = findUpStation();
		stations.add(upStation);

		while (doesExistDownSection(upStation)) {
			Section downSection = findDownStation(upStation);
			upStation = downSection.getDownStation();
			stations.add(upStation);
		}

		return stations.stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private boolean doesExistDownSection(Station station) {
		return sections.stream().anyMatch(section -> section.getUpStation().equals(station));
	}

	private Section findDownStation(Station station) {
		return sections.stream().filter(section -> section.getUpStation().equals(station))
				.findFirst()
				.orElse(null);
	}

	private Station findUpStation() {
		Station upStation = sections.get(0).getUpStation();
		while (doesExistUpperSection(upStation)) {
			Section upperSection = findUpperStation(upStation);
			upStation = upperSection.getUpStation();
		}
		return upStation;
	}

	private Section findUpperStation(Station station) {
		return sections.stream().filter(section -> section.getDownStation().equals(station))
				.findFirst()
				.orElse(null);
	}

	private boolean doesExistUpperSection(Station station) {
		return sections.stream().anyMatch(section -> section.getDownStation().equals(station));
	}
}
