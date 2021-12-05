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
		return getStations().stream().anyMatch(it -> it == station);
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
			.ifPresent(it -> it.updateUpStation(downStation, distance));
	}

	private void updateDownStation(Station upStation, Station downStation, int distance) {
		findFirst(section -> section.equalsDownStation(downStation))
			.ifPresent(it -> it.updateDownStation(upStation, distance));
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
			.anyMatch(it -> it.equalsUpStation(station));
	}

	private boolean hasSectionHavingDownStation(Station station) {
		return sections.stream()
			.filter(Section::hasDownStation)
			.anyMatch(it -> it.equalsDownStation(station));
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
