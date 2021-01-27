package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.station.domain.Station;

@Entity
public class Section implements Comparable<Section> {
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

	private Distance distance;

	protected Section() {
	}

	public Section(Line line, Station upStation, Station downStation, int distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = new Distance(distance);
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public List<Station> getStations() {
		return Arrays.asList(upStation, downStation);
	}

	public boolean containsAll(List<Station> stations) {
		return getStations().containsAll(stations);
	}

	public Station getUpStation() {
		return upStation;
	}

	public boolean isUpStation(Station station) {
		return upStation.equals(station);
	}

	public Station getDownStation() {
		return downStation;
	}

	public boolean isDownStation(Station station) {
		return downStation.equals(station);
	}

	public int getDistance() {
		return distance.value();
	}

	public int plusDistance(Section section) {
		return distance.plus(section.getDistance()).value();
	}

	public void updateUpStation(Station station, int distance) {
		this.upStation = station;
		this.distance = this.distance.minus(distance);
	}

	public void updateDownStation(Station station, int distance) {
		this.downStation = station;
		this.distance = this.distance.minus(distance);
	}

	@Override
	public int compareTo(Section section) {
		if (section.isDownStation(downStation)) {
			return 0;
		}

		if (section.isDownStation(this.upStation)) {
			return 1;
		}

		return -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Section section = (Section)o;
		return Objects.equals(id, section.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
