package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public List<Section> getSections() {
		return sections;
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}

	public List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = this.findUpStation();
		stations.add(downStation);

		List<Station> nextStations = this.getSortedDownStations(downStation);
		stations.addAll(nextStations);
		return stations;
	}

	private List<Station> getSortedDownStations(Station downStation) {
		List<Station> nextStations = new ArrayList<>();
		Station station = downStation;

		Optional<Section> downSectionOpt = this.findDownSection(station);
		while (downSectionOpt.isPresent()) {
			Section downSection = downSectionOpt.get();
			station = downSection.getDownStation();
			nextStations.add(station);
			downSectionOpt = this.findDownSection(station);
		}
		return nextStations;
	}

	private Station findUpStation() {
		Station currentStation = this.sections.get(0).getUpStation();

		Optional<Section> upSectionOpt = this.findUpSection(currentStation);
		while (upSectionOpt.isPresent()) {
			Section upSection = upSectionOpt.get();
			currentStation = upSection.getUpStation();
			upSectionOpt = this.findUpSection(currentStation);
		}
		return currentStation;
	}

	private Optional<Section> findDownSection(Station station) {
		return this.sections.stream()
			.filter(section -> section.isLinkedDownSection(station))
			.findFirst();
	}

	private Optional<Section> findUpSection(Station station) {
		return this.sections.stream()
			.filter(section -> section.isLinkedUpSection(station))
			.findFirst();
	}
}
