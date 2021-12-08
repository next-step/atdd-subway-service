package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	public static final int SECTIONS_MIN_SIZE_INCLUSIVE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public void add(Section section) {
		connect(section.getUpStation(), section.getDownStation(), section.getDistance());
		sections.add(section);
	}

	private void connect(Station upStation, Station downStation, int distance) {
		if (sections.isEmpty()) {
			return;
		}
		boolean isUpStationExisted = isExisted(upStation);
		boolean isDownStationExisted = isExisted(downStation);
		validateToConnect(isUpStationExisted, isDownStationExisted);

		if (isUpStationExisted) {
			updateUpStation(upStation, downStation, distance);
		}
		if (isDownStationExisted) {
			updateDownStation(upStation, downStation, distance);
		}
	}

	private boolean isExisted(Station station) {
		return getStations().stream().anyMatch(existentStation -> existentStation == station);
	}

	private void validateToConnect(boolean isUpStationExisted, boolean isDownStationExisted) {
		if (isUpStationExisted && isDownStationExisted) {
			throw new IllegalArgumentException("이미 연결된 구간 입니다.");
		}
		if (!isUpStationExisted && !isDownStationExisted) {
			throw new IllegalArgumentException("연결할 수 없는 구간 입니다.");
		}
	}

	private void updateUpStation(Station upStation, Station downStation, int distance) {
		findFirst(section -> section.equalsUpStation(upStation))
			.ifPresent(section -> section.updateUpStation(downStation, distance));
	}

	private void updateDownStation(Station upStation, Station downStation, int distance) {
		findFirst(section -> section.equalsDownStation(downStation))
			.ifPresent(section -> section.updateDownStation(upStation, distance));
	}

	public void delete(Station station) {
		validateToDelete();
		final Optional<Section> maybeUpSection = findFirst(section -> section.equalsUpStation(station));
		final Optional<Section> maybeDownSection = findFirst(section -> section.equalsDownStation(station));
		maybeUpSection.ifPresent(upSection ->
			maybeDownSection.ifPresent(downSection -> sections.add(createSection(upSection, downSection)))
		);
		maybeUpSection.ifPresent(section -> sections.remove(section));
		maybeDownSection.ifPresent(section -> sections.remove(section));
	}

	private void validateToDelete() {
		if (sections.size() <= SECTIONS_MIN_SIZE_INCLUSIVE) {
			throw new IllegalArgumentException("노선에서 유일한 구간의 역은 제거할 수 없습니다.");
		}
	}

	private Section createSection(Section sectionToDeleteUpStation, Section sectionToDeleteDownStation) {
		final Line line = sectionToDeleteUpStation.getLine();
		final Station upStation = sectionToDeleteDownStation.getUpStation();
		final Station downStation = sectionToDeleteUpStation.getDownStation();
		final int distance = sectionToDeleteUpStation.getDistance() + sectionToDeleteDownStation.getDistance();
		return new Section(line, upStation, downStation, distance);
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Collections.emptyList();
		}
		return getStationsSortedByUpToDown();
	}

	private List<Station> getStationsSortedByUpToDown() {
		final List<Station> stations = new ArrayList<>();
		Station station = findUpTerminalStation();
		stations.add(station);

		while (hasSectionHavingUpStation(station)) {
			final Section nextSection = findSectionHavingUpStation(station);
			station = nextSection.getDownStation();
			stations.add(station);
		}
		return stations;
	}

	private Station findUpTerminalStation() {
		Station upStation = sections.get(0).getUpStation();
		while (hasSectionHavingDownStation(upStation)) {
			final Section preSection = findSectionHavingDownStation(upStation);
			upStation = preSection.getUpStation();
		}
		return upStation;
	}

	private boolean hasSectionHavingUpStation(Station station) {
		return sections.stream()
			.filter(Section::hasUpStation)
			.anyMatch(section -> section.equalsUpStation(station));
	}

	private boolean hasSectionHavingDownStation(Station station) {
		return sections.stream()
			.filter(Section::hasDownStation)
			.anyMatch(section -> section.equalsDownStation(station));
	}

	private Section findSectionHavingUpStation(Station station) {
		return findFirst(section -> section.equalsUpStation(station))
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("%s을 상행역으로 갖는 구간이 없습니다.", station.getName())
			));
	}

	private Section findSectionHavingDownStation(Station station) {
		return findFirst(section -> section.equalsDownStation(station))
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("%s을 하행역으로 갖는 구간이 없습니다.", station.getName())
			));
	}

	private Optional<Section> findFirst(Predicate<? super Section> conditional) {
		return sections.stream().filter(conditional).findFirst();
	}
}
