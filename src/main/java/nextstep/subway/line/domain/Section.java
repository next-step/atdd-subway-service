package nextstep.subway.line.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

@Entity
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "line_id")
	private Line line;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "down_station_id")
	private Station downStation;

	@Embedded
	private Distance distance;

	protected Section() {
	}

	public Section(Line line, Station upStation, Station downStation, Distance distance) {
		validate(line, upStation, downStation, distance);
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public Station getUpStation() {
		return upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public Distance getDistance() {
		return distance;
	}

	public boolean isUpStationExisted(List<Station> stations) {
		return stations.stream().anyMatch(it -> it == this.upStation);
	}

	public boolean isDownStationExisted(List<Station> stations) {
		return stations.stream().anyMatch(it -> it == this.downStation);
	}

	public void updateUpStation(Station station, Distance distance) {
		this.upStation = station;
		this.distance = this.distance.minus(distance);
	}

	public void updateDownStation(Station station, Distance distance) {
		this.downStation = station;
		this.distance = this.distance.minus(distance);
	}

	public boolean isEqualsDownStation(Station station) {
		return this.downStation.equals(station);
	}

	public boolean isEqualsUpStation(Station station) {
		return this.upStation.equals(station);
	}

	public boolean isFirstSection(List<Section> sections) {
		return sections.stream().noneMatch(section -> this.isEqualsUpStation(section.getDownStation()));
	}

	private void validate(Line line, Station upStation, Station downStation, Distance distance) {
		if(!Objects.nonNull(line) || !Objects.nonNull(upStation) || !Objects.nonNull(downStation) || !Objects.nonNull(distance)) {
			throw new RuntimeException("노선 또는 상행역, 하행역, 거리가 비워져 있습니다.");
		}

	}

}
