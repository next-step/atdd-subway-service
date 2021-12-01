package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.exception.SectionException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	private static final int ONLY_ONE_SECTION_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSectionList() {
		return sections;
	}

	public void addSection(final Section section) {
		if (!sections.isEmpty()) {
			validSameUpDownStation(section);
			validIsNotInStations(section);
			sections
				.stream()
				.filter(inner -> !inner.isSameUpDownStation(section))
				.findAny()
				.ifPresent(inner -> inner.reSettingSection(section));
		}

		sections.add(section);
	}

	private void validSameUpDownStation(final Section section) {
		if (isSameUpDownStation(section)) {
			throw new SectionException(ErrorCode.IS_SAME_UP_DOWN_STATION_ERROR);
		}
	}

	private void validIsNotInStations(final Section section) {
		if (!isInUpDownStation(section)) {
			throw new SectionException(ErrorCode.IS_NOT_IN_STATIONS_ERROR);
		}
	}

	private boolean isSameUpDownStation(final Section section) {
		return sections.stream()
			.anyMatch(it -> it.isSameUpDownStation(section));
	}

	private boolean isInUpStation(final Section section) {
		return sections.stream()
			.anyMatch(it -> it.isInUpDownStation(section.getUpStation()));
	}

	private boolean isInDownStation(final Section section) {
		return sections.stream()
			.anyMatch(it -> it.isInUpDownStation(section.getDownStation()));
	}

	private boolean isInUpDownStation(final Section section) {
		return isInUpStation(section) || isInDownStation(section);
	}

	public List<Station> getUpStations() {
		return sections.stream()
			.map(Section::getUpStation)
			.distinct()
			.collect(Collectors.toList());
	}

	public List<Station> getDownStations() {
		return sections.stream()
			.map(Section::getDownStation)
			.distinct()
			.collect(Collectors.toList());
	}

	private Section findFirstSection() {
		return sections.stream()
			.filter(it -> !getDownStations().contains(it.getUpStation()))
			.findFirst()
			.orElseThrow(() -> new SectionException(ErrorCode.IS_NOT_IN_STATIONS_ERROR));
	}

	private Section findLastSection() {
		return sections.stream()
			.filter(it -> !getUpStations().contains(it.getDownStation()))
			.findFirst()
			.orElseThrow(() -> new SectionException(ErrorCode.IS_NOT_IN_STATIONS_ERROR));
	}

	private boolean isNotLastDownStation(Station station) {
		return !findLastSection().isSameDownStation(station);
	}

	private Section findUpSection(Station downStation) {
		return sections.stream()
			.filter(it -> it.isSameUpStation(downStation))
			.findFirst()
			.orElseThrow(() -> new SectionException(ErrorCode.IS_NOT_IN_STATIONS_ERROR));
	}

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();

		Section section = findFirstSection();
		stations.add(section.getUpStation());

		while (isNotLastDownStation(section.getDownStation())) {
			Section extractSection = findUpSection(section.getDownStation());
			stations.add(extractSection.getUpStation());
			section = extractSection;
		}

		stations.add(section.getDownStation());

		return stations;
	}

	public void removeStation(Station removeStation) {
		validRemoveStation(removeStation);

		Optional<Section> upLineStation = getUpLineStation(removeStation);
		Optional<Section> downLineStation = getDownLineStation(removeStation);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			resettingRemoveSections(upLineStation.get(), downLineStation.get());
		}

		upLineStation.ifPresent(it -> sections.remove(it));
		downLineStation.ifPresent(it -> sections.remove(it));
	}

	private Optional<Section> getUpLineStation(Station removeStation) {
		return sections.stream()
			.filter(it -> it.getUpStation().equals(removeStation))
			.findFirst();
	}

	private Optional<Section> getDownLineStation(Station removeStation) {
		return sections.stream()
			.filter(it -> it.getDownStation().equals(removeStation))
			.findFirst();
	}

	private void resettingRemoveSections(Section reSettingUpLineStation, Section reSettingDownLineStation) {
		Station newUpStation = reSettingDownLineStation.getUpStation();
		Station newDownStation = reSettingUpLineStation.getDownStation();
		reSettingUpLineStation.plusDistance(reSettingDownLineStation);
		sections.add(
			new Section(reSettingUpLineStation.getLine(), newUpStation, newDownStation,
				reSettingUpLineStation.getDistance()));
	}

	public void validRemoveStation(Station removeStation) {
		validHasOneSection();
		validNotInSection(removeStation);
	}

	private void validHasOneSection() {
		if (sections.size() == ONLY_ONE_SECTION_SIZE) {
			throw new SectionException(ErrorCode.VALID_CAN_NOT_REMOVE_LAST_STATION);
		}
	}

	private void validNotInSection(Station removeStation) {
		if (!getStations().contains(removeStation)) {
			throw new SectionException(ErrorCode.VALID_CAN_NOT_REMOVE_NOT_IN_STATIONS);
		}
	}

	private boolean isBetweenStations(Station removeStation) {
		return getUpStations().contains(removeStation) && getDownStations().contains(removeStation);
	}
}
