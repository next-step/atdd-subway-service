package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.SortedStations;
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

	private void validateAlreadyExistSection(Section section) {
		if (this.getSortedStations().containsAll(section)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}
	}

	private void validateNotContainedStations(Section section) {
		if (this.getSortedStations().isUnRelatedSection(section)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	public void addSection(Section newSection) {
		this.validateAlreadyExistSection(newSection);
		this.validateNotContainedStations(newSection);

		if (this.getSortedStations().isEmpty()) {
			this.sections.add(newSection);
			return;
		}
		this.rebuildSection(newSection);
	}

	private void rebuildSection(Section newSection) {
		this.sections.stream()
			.filter(section -> section.isBuildable(newSection))
			.findFirst()
			.ifPresent(section -> section.rebuild(newSection));
		this.sections.add(newSection);
	}

	public SortedStations getSortedStations() {
		SortedStations sortedStations = new SortedStations();
		if (this.sections.isEmpty()) {
			return sortedStations;
		}

		Station downStation = this.findUpStation();
		sortedStations.addStation(downStation);

		this.addDownStations(sortedStations, downStation);
		return sortedStations;
	}

	private void addDownStations(SortedStations sortedStations, Station finalDownStation) {
		Optional<Section> downSectionOpt = this.findDownSection(finalDownStation);
		while (downSectionOpt.isPresent()) {
			Section downSection = downSectionOpt.get();
			finalDownStation = downSection.getDownStation();
			sortedStations.addStation(finalDownStation);
			downSectionOpt = this.findDownSection(finalDownStation);
		}
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
