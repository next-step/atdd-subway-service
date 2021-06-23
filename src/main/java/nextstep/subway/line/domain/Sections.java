package nextstep.subway.line.domain;

import nextstep.subway.exception.CustomException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static nextstep.subway.exception.CustomExceptionMessage.*;

@Embeddable
public class Sections {

	private final static int MIN_SECTION_COUNT = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();


	public List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return Collections.emptyList();
		}
		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextSection = findSections(it -> it.isMatchUpStation(finalDownStation));
			if (!nextSection.isPresent()) {
				break;
			}
			downStation = nextSection.get().getDownStation();
			stations.add(downStation);
		}
		return stations;
	}

	public void addSection(final Line line, final Station upStation, final Station downStation, final int distance) {
		if (this.sections.isEmpty()) {
			addNewSectionInstance(new Section(line, upStation, downStation, distance));
			return;
		}
		boolean isUpStationExisted = isExistedStation(upStation);
		boolean isDownStationExisted = isExistedStation(downStation);
		checkExistedAllStations(isUpStationExisted, isDownStationExisted);
		checkNotExistedAllStations(upStation, downStation);
		if (isUpStationExisted) {
			connectToSectionOfUpStation(line, upStation, downStation, distance);
			return;
		}
		if (isDownStationExisted) {
			connectToSectionOfDownStation(line, upStation, downStation, distance);
			return;
		}
		throw new CustomException(NONE);
	}

	public void removeSection(final Line line, final Station station) {
		checkMinSectionSize();
		Optional<Section> upSection = findSections(it -> it.isMatchUpStation(station));
		Optional<Section> downSection = findSections(it -> it.isMatchDownStation(station));
		if (upSection.isPresent() && downSection.isPresent()) {
			Station newUpStation = downSection.get().getUpStation();
			Station newDownStation = upSection.get().getDownStation();
			int newDistance = upSection.get().getDistance() + downSection.get().getDistance();
			addNewSectionInstance(new Section(line, newUpStation, newDownStation, newDistance));
		}
		upSection.ifPresent(this::removeSectionInstance);
		downSection.ifPresent(this::removeSectionInstance);
	}

	private void checkMinSectionSize() {
		if (this.sections.size() <= MIN_SECTION_COUNT) {
			throw new CustomException(IMPOSSIBLE_MIN_SECTION_SIZE);
		}
	}

	private void checkExistedAllStations(final boolean isUpStationExisted, final boolean isDownStationExisted) {
		if (isUpStationExisted && isDownStationExisted) {
			throw new CustomException(EXIST_ALL_STATION_IN_SECTIONS);
		}
	}

	private void checkNotExistedAllStations(final Station upStation, final Station downStation) {
		if (isNotExistedStation(upStation) && isNotExistedStation(downStation)) {
			throw new CustomException(NOT_EXIST_ALL_STATION_IN_SECTIONS);
		}
	}

	private void connectToSectionOfUpStation(final Line line, final Station upStation, final Station downStation, final int distance) {
		findSections(it -> it.isMatchUpStation(upStation))
			.ifPresent(it -> it.updateUpStation(downStation, distance));
		addNewSectionInstance(new Section(line, upStation, downStation, distance));
	}

	private void connectToSectionOfDownStation(final Line line, final Station upStation, final Station downStation, final int distance) {
		findSections(it -> it.isMatchDownStation(downStation))
			.ifPresent(it -> it.updateDownStation(upStation, distance));
		addNewSectionInstance(new Section(line, upStation, downStation, distance));
	}

	private void addNewSectionInstance(final Section section) {
		this.sections.add(section);
	}

	private void removeSectionInstance(final Section section) {
		this.sections.remove(section);
	}

	private boolean isExistedStation(final Station station) {
		return this.sections.stream()
							.flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
							.anyMatch(it -> it == station);
	}

	private boolean isNotExistedStation(final Station station) {
		return this.sections.stream()
							.flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
							.noneMatch(it -> it == station);
	}

	private Station findUpStation() {
		Station downStation = this.sections.get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextSection = findSections(it -> it.isMatchDownStation(finalDownStation));
			if (!nextSection.isPresent()) {
				break;
			}
			downStation = nextSection.get().getUpStation();
		}
		return downStation;
	}

	private Optional<Section> findSections(Predicate<Section> condition) {
		return this.sections.stream()
							.filter(condition)
							.findFirst();
	}
}
