package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class LineSections {
	
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected LineSections() {
	}

	public LineSections(List<Section> sections) {
		this.sections.addAll(sections);
	}

	public void add(Section section) {
		this.sections.add(section);
	}

	public void addSection(Section newSection) {
		boolean isUpStationExisted = isContainStation(newSection.getUpStation());
		boolean isDownStationExisted = isContainStation(newSection.getDownStation());
		validateSection(isUpStationExisted, isDownStationExisted);

		if (isUpStationExisted) {
			addSectionBasedUpStation(newSection);
			return;
		}
		addSectionBasedDownStation(newSection);
	}

	public void addSectionBasedUpStation(Section newSection) {
		findByUpStation(newSection.getUpStation())
			.ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	public void addSectionBasedDownStation(Section newSection) {
		findByDownStation(newSection.getDownStation())
			.ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
		this.sections.add(newSection);
	}

	public void removeStation(Line line, Station station) {
		if (!isRemovable()) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = this.findByUpStation(station);
		Optional<Section> downLineStation = this.findByDownStation(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			removeMiddleStation(line, upLineStation.get(), downLineStation.get());
		}

		upLineStation.ifPresent(it -> this.getSections().remove(it));
		downLineStation.ifPresent(it -> this.getSections().remove(it));
	}

	public boolean isRemovable() {
		return this.sections.size() > 1;
	}

	public List<Section> getSections() {
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
			Optional<Section> nextLineStation = findByUpStation(finalUpStation);
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

	private Optional<Section> findByUpStation(Station upStation) {
		return this.sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst();
	}

	private Optional<Section> findByDownStation(Station downStation) {
		return this.sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst();
	}

	private Station findUpStation() {
		Station upStation = this.sections.get(0).getUpStation();
		while (upStation != null) {
			Station finalDownStation = upStation;
			Optional<Section> nextLineStation = findByDownStation(finalDownStation);
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

	private void removeMiddleStation(Line line, Section upStationSection, Section downStationSection) {
		Station newUpStation = downStationSection.getUpStation();
		Station newDownStation = upStationSection.getDownStation();
		int newDistance = upStationSection.getDistance() + downStationSection.getDistance();
		add(new Section(line, newUpStation, newDownStation, newDistance));
	}
}
