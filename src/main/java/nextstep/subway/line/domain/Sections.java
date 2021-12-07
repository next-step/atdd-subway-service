package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
		CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public Sections(Section... sections) {
		for (Section section : sections) {
			this.sections.add(section);
		}
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}
		List<Station> stations = new ArrayList<>();
		Section firstSection = findFirstSection();
		addStationsInFirstSection(firstSection, stations);
		addAllStationsExceptFirstSection(firstSection, stations);
		return stations;
	}

	private void addStationsInFirstSection(Section firstSection, List<Station> stations) {
		stations.add(firstSection.getUpStation());
		stations.add(firstSection.getDownStation());
	}

	private void addAllStationsExceptFirstSection(Section currentSection, List<Station> stations) {
		while (isNextSection(currentSection)) {
			Section finalCurrentSection = currentSection;
			currentSection = sections.stream()
				.filter(section -> section.getUpStation().equals(finalCurrentSection.getDownStation()))
				.findFirst()
				.orElse(finalCurrentSection);
			stations.add(currentSection.getDownStation());
		}
	}

	private boolean isNextSection(Section currentSection) {
		return sections.stream()
			.anyMatch(section -> section.getUpStation().equals(currentSection.getDownStation()));
	}

	private boolean isFirstSection(Section firstSection) {
		return sections.stream()
			.noneMatch(section -> section.getDownStation().equals(firstSection.getUpStation()));
	}

	private Section findFirstSection() {
		Section firstSection = sections.get(0);
		while (!isFirstSection(firstSection)) {
			Section finalFistSection = firstSection;
			firstSection = sections.stream()
				.filter(section -> section.getDownStation().equals(finalFistSection.getUpStation()))
				.findFirst()
				.orElse(finalFistSection);
		}
		return firstSection;
	}

	public void addSection(Section section) {
		validateStationsExisted(section);
		validateStationNonMatch(section);

		if (getStations().isEmpty()) {
			sections.add(section);
			return;
		}

		if (isUpStationExisted(section)) {
			updateUpStation(section);
			sections.add(section);
			return;
		}

		if (isDownStationExisted(section)) {
			updateDownStation(section);
			sections.add(section);
			return;
		}
	}

	private void updateDownStation(Section addSection) {
		sections.stream()
			.filter(section -> section.getDownStation().equals(addSection.getDownStation()))
			.findFirst()
			.ifPresent(section -> section.updateDownStation(addSection.getUpStation(), addSection.getDistance()));
	}

	private void updateUpStation(Section addSection) {
		sections.stream()
			.filter(section -> section.getUpStation().equals(addSection.getUpStation()))
			.findFirst()
			.ifPresent(section -> section.updateUpStation(addSection.getDownStation(), addSection.getDistance()));
	}

	private boolean isDownStationExisted(Section section) {
		return getStations().stream()
			.anyMatch(station -> station.equals(section.getDownStation()));
	}

	private boolean isUpStationExisted(Section section) {
		return getStations().stream()
			.anyMatch(station -> station.equals(section.getUpStation()));
	}

	private void validateStationNonMatch(Section section) {
		if (getStations().stream()
			.noneMatch(station -> station.equals(section.getUpStation()) || station.equals(section.getDownStation()))) {
			throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
		}
	}

	private void validateStationsExisted(Section section) {
		if (getStations().containsAll(Arrays.asList(section.getUpStation(), section.getDownStation()))) {
			throw new IllegalArgumentException("이미 등록된 구간 입니다.");
		}
	}

	public void remove(Line line, Station station) {
		validateSectionsSize();
		Optional<Section> upSection = findOptionalUpSection(station);
		Optional<Section> downSection = findOptionalDownSection(station);

		if (isMiddleStation(station)) {
			Station newUpStation = getUpStation(downSection);
			Station newDownStation = getDownStation(upSection);
			int newSectionDistance = getNewSectionDistance(station);
			sections.add(new Section(line, newUpStation, newDownStation, newSectionDistance));
		}

		upSection.ifPresent(section -> sections.remove(section));
		downSection.ifPresent(section -> sections.remove(section));
	}

	private int getNewSectionDistance(Station station) {
		return sections.stream()
			.filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
			.mapToInt(Section::getDistance)
			.sum();
	}

	private Station getUpStation(Optional<Section> optionalSection) {
		return optionalSection.orElseThrow(() -> new IllegalArgumentException("구간이 존재하지 않습니다."))
		.getUpStation();
	}

	private Station getDownStation(Optional<Section> optionalSection) {
		return optionalSection.orElseThrow(() -> new IllegalArgumentException("구간이 존재하지 않습니다."))
			.getDownStation();
	}

	private Optional<Section> findOptionalDownSection(Station station) {
		return sections.stream()
			.filter(it -> it.getDownStation().equals(station))
			.findFirst();
	}

	private Optional<Section> findOptionalUpSection(Station station) {
		return sections.stream()
			.filter(section -> section.getUpStation().equals(station))
			.findFirst();
	}

	private boolean isMiddleStation(Station station) {
		return sections.stream()
			.filter(section -> section.getDownStation().equals(station) || section.getUpStation().equals(station))
			.count() == 2;
	}

	private void validateSectionsSize() {
		if (this.sections.size() <= 1) {
			throw new IllegalArgumentException("삭제 후 노선 내 구간은 1개 이상 존재해야 합니다.");
		}
	}
}
