package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import nextstep.subway.station.domain.Station;

public class LineSections {
	List<SectionNew> sections = new ArrayList<>();

	public LineSections(List<SectionNew> sections) {
		this.sections.addAll(sections);
	}

	public void add(SectionNew section) {
		this.sections.add(section);
	}

	public void addSection(SectionNew newSection) {
		boolean isUpStationExisted = isContainStation(newSection.getUpStation());
		boolean isDownStationExisted = isContainStation(newSection.getDownStation());
		validateSection(isUpStationExisted, isDownStationExisted);

		if (isUpStationExisted) {
			addSectionBasedUpStation(newSection);
			return;
		}
		if (isDownStationExisted) {
			addSectionBasedDownStation(newSection);
			return;
		}
		throw new RuntimeException();
	}

	public void addSectionBasedUpStation(SectionNew newSection) {
		findByUpStation(newSection.getUpStation())
			.ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	public void addSectionBasedDownStation(SectionNew newSection) {
		findByDownStation(newSection.getDownStation())
			.ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	public List<SectionNew> getSections() {
		return this.sections;
	}

	public List<Station> getStations() {
		if (this.sections.isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalUpStation = downStation;
			Optional<SectionNew> nextLineStation = findByUpStation(finalUpStation);
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getDownStation();
			stations.add(downStation);
		}

		return stations;
	}

	private boolean isContainStation(Station station) {
		return this.getStations().stream()
			.anyMatch(st -> st.equals(station));
	}

	private Optional<SectionNew> findByUpStation(Station upStation) {
		return this.sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst();
	}

	private Optional<SectionNew> findByDownStation(Station downStation) {
		return this.sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst();
	}

	private Station findUpStation() {
		Station upStation = this.sections.get(0).getUpStation();
		while (upStation != null) {
			Station finalDownStation = upStation;
			Optional<SectionNew> nextLineStation = findByDownStation(finalDownStation);
			if (!nextLineStation.isPresent()) {
				break;
			}
			upStation = nextLineStation.get().getUpStation();
		}

		return upStation;
	}

	private void validateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!isUpStationExisted && !isDownStationExisted) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}
}
