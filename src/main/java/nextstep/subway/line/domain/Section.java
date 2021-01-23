package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.Message;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

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

	private int distance;

	public Section() {
	}

	protected Section(Line line, Station upStation, Station downStation, int distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
		Stations stations = line.getStations();
		validateStation(stations, upStation, downStation);
		Section section = new Section(line, upStation, downStation, distance);
		validateSection(stations, section);
		return section;
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

	private static void validateStation(Stations stations, Station upStation, Station downStation) {
		if (stations.isContains(upStation) && stations.isContains(downStation)) {
			throw new RuntimeException(Message.EXIST_SECTION);
		}

		if (stations.isNotEmpty() && stations.isNotContains(upStation) && stations.isNotContains(downStation)) {
			throw new RuntimeException(Message.INVALID_SECTION);
		}
	}

	private static void validateSection(Stations stations, Section section) {
		Line line = section.getLine();

		if (stations.isEmpty()
			|| line.updateSectionStation(section)) {
			return;
		}

		throw new RuntimeException();
	}
}
