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

import nextstep.subway.Message;
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

	private int distance;

	public Section() {
	}

	public Section(Line line, Station upStation, Station downStation, int distance) {
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

	public List<Station> getStations() {
		return Arrays.asList(upStation, downStation);
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
		return distance;
	}

	public void updateUpStation(Station station, int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException(Message.PLEASE_ENTER_SHORTER_DISTANCE);
		}
		this.upStation = station;
		this.distance -= newDistance;
	}

	public void updateDownStation(Station station, int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException(Message.PLEASE_ENTER_SHORTER_DISTANCE);
		}
		this.downStation = station;
		this.distance -= newDistance;
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
		return distance == section.distance &&
			Objects.equals(id, section.id) &&
			Objects.equals(line, section.line) &&
			Objects.equals(upStation, section.upStation) &&
			Objects.equals(downStation, section.downStation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, line, upStation, downStation, distance);
	}
}
