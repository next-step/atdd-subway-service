package nextstep.subway.line.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Line extends BaseEntity {
	@Transient
	private static final int MINIMAL_SECTION_COUNT = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	@Embedded
	private Sections sections = new Sections();

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	@Builder
	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		sections.addSection(this, upStation, downStation, distance);
	}

	public Line(long id, String name, String color, Station upStation, Station downStation, int distance) {
		this.id = id;
		this.name = name;
		this.color = color;
		sections.addSection(this, upStation, downStation, distance);
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	private void updateUpStation(Station upStation, Station downStation, int distance) {
		sections.updateUpStation(upStation, downStation, distance);
	}

	private void updateDownStation(Station upStation, Station downStation, int distance) {
		sections.updateDownStation(upStation, downStation, distance);
	}

	private Section getSectionEqualUpStation(Station station) {
		return sections.getSectionEqualUpStation(station);
	}

	private Section getSectionEqualDownStation(Station station) {
		return sections.getSectionEqualDownStation(station);
	}

	public void removeLineStation(Station station) {
		Section upLineStation = getSectionEqualUpStation(station);
		Section downLineStation = getSectionEqualDownStation(station);

		if (upLineStation != null && downLineStation != null) {
			addMergedSection(upLineStation, downLineStation);
		}

		sections.remove(upLineStation);
		sections.remove(downLineStation);
	}

	private void addMergedSection(Section upLineStation, Section downLineStation) {
		Station newUpStation = downLineStation.getUpStation();
		Station newDownStation = upLineStation.getDownStation();
		int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
		sections.addSection(this, newUpStation, newDownStation, newDistance);
	}

	public void addSection(Station upStation, Station downStation, int distance) {
		List<Station> stations = getStations();
		validateSections(stations, upStation, downStation);
		if (stations.isEmpty()) {
			sections.addSection(this, upStation, downStation, distance);
			return;
		}

		updateStation(stations, upStation, downStation, distance);
		sections.addSection(this, upStation, downStation, distance);
	}

	private void updateStation(List<Station> stations, Station upStation, Station downStation, int distance) {
		if (upStation.isExisted(stations)) {
			updateUpStation(upStation, downStation, distance);
		} else if (downStation.isExisted(stations)) {
			updateDownStation(upStation, downStation, distance);
		} else {
			throw new RuntimeException();
		}
	}

	private void validateSections(List<Station> stations, Station upStation, Station downStation) {
		if (upStation.isExisted(stations) && downStation.isExisted(stations)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
			stations.stream().noneMatch(it -> it == downStation)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	public List<Section> getSections() {
		return sections.getSections();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public boolean isSectionsExists() {
		return getSections().size() > 1;
	}
}
