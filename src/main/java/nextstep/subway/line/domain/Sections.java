package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
	private static final int FIRST_SECTION_INDEX = 0;
	private static final int LEAST_SIZE_TO_REMOVE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSections() {
		return sections;
	}

	public void add(Section section) {
		sections.add(section);
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}

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
		Station upStation = sections.get(FIRST_SECTION_INDEX).getUpStation();
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

	public void addSection(Section section) {
		boolean isUpStationExisted = doesExistStation(section.getUpStation());
		boolean isDownStationExisted = doesExistStation(section.getDownStation());
		validateToAddSection(isUpStationExisted, isDownStationExisted);

		if (isUpStationExisted) {
			updateSectionWhenUpStaionExist(section);
		}

		if (isDownStationExisted) {
			updateSectionWhenDownStationExist(section);
		}
		sections.add(section);
	}

	private void updateSectionWhenDownStationExist(Section section) {
		getSections().stream().filter(existedSection -> existedSection.getDownStation().equals(section.getDownStation()))
				.findFirst()
				.ifPresent(existedSection -> existedSection.updateDownStation(section.getUpStation(), section.getDistance()));
	}

	private void updateSectionWhenUpStaionExist(Section section) {
		getSections().stream().filter(existedSection -> existedSection.getUpStation().equals(section.getUpStation()))
				.findFirst()
				.ifPresent(existedSection -> existedSection.updateUpStation(section.getDownStation(), section.getDistance()));
	}

	private void validateToAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!getStations().isEmpty() && !isUpStationExisted && !isDownStationExisted) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private boolean doesExistStation(Station station) {
		return getStations().stream().anyMatch(existedStation -> existedStation.equals(station));
	}

	public void removeStation(Line line, Station station) {
		validateRemoving();

		Optional<Section> upLineStation = getUpStationMatchedBy(station);
		Optional<Section> downLineStation = getDownStationMatchedBy(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			sections.add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> line.getSections().remove(it));
		downLineStation.ifPresent(it -> line.getSections().remove(it));
	}

	private Optional<Section> getDownStationMatchedBy(Station station) {
		return sections.stream()
				.filter(it -> it.getDownStation().equals(station))
				.findFirst();
	}

	private Optional<Section> getUpStationMatchedBy(Station station) {
		return sections.stream()
				.filter(it -> it.getUpStation().equals(station))
				.findFirst();
	}

	private void validateRemoving() {
		if (sections.size() <= LEAST_SIZE_TO_REMOVE) {
			throw new RuntimeException();
		}
	}
}
