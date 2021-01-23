package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Line(String name, String color, Station upStation, Station downStation, int distance) {
		this.name = name;
		this.color = color;
		sections.add(Section.createSection(this, upStation, downStation, distance));
	}

	public void update(Line line) {
		this.name = line.getName();
		this.color = line.getColor();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public boolean isSectionsSizeLessThanOrEqualTo(int size) {
		return sections.size() <= size;
	}

	public void addSection(Section section) {
		sections.add(section);
	}

	public void removeSection(Section section) {
		sections.remove(section);
	}

	public Stations getStations() {
		if (sections.isEmpty()) {
			return new Stations();
		}

		List<Station> stations = new ArrayList<>();
		Station station = findFirstUpStation();

		while (Objects.nonNull(station)) {
			stations.add(station);
			station = findDownStationByUpStation(station);
		}

		return new Stations(stations);
	}

	public boolean updateSectionStation(Section section) {
		Stations stations = getStations();

		if (stations.isContains(section.getUpStation())) {
			updateUpStation(section);
			return true;
		}

		if (stations.isContains(section.getDownStation())) {
			updateDownStation(section);
			return true;
		}

		return false;
	}

	private void updateUpStation(Section section) {
		findSectionByUpStation(section.getUpStation())
			.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
	}

	private void updateDownStation(Section section) {
		findSectionByDownStation(section.getDownStation())
			.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));
	}

	public Optional<Section> findSectionByUpStation(Station upStation) {
		return sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst();
	}

	public Optional<Section> findSectionByDownStation(Station downStation) {
		return sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst();
	}

	private Station findFirstUpStation() {
		Station upStation = null;
		Station station = sections.get(0).getUpStation();

		while (Objects.nonNull(station)) {
			upStation = station;
			station = findUpStationByDownStation(upStation);
		}

		return upStation;
	}

	private Station findUpStationByDownStation(Station downStation) {
		return sections.stream()
			.filter(it -> it.isDownStation(downStation))
			.findFirst()
			.map(Section::getUpStation)
			.orElse(null);
	}

	private Station findDownStationByUpStation(Station upStation) {
		return sections.stream()
			.filter(it -> it.isUpStation(upStation))
			.findFirst()
			.map(Section::getDownStation)
			.orElse(null);
	}
}
