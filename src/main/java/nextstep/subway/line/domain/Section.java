package nextstep.subway.line.domain;

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

	private Section() {
	}

	private Section(Line line, Station upStation, Station downStation, Distance distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static Section from() {
		return new Section();
	}

	public static Section of(Line line, Station upStation, Station downStation, int distance) {
		return new Section(line, upStation, downStation, Distance.from(distance));
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

	public int getDistance() {
		return distance.getDistance();
	}

	public boolean isSameUpStation(Station upStation) {
		return this.upStation.equals(upStation);
	}

	public boolean isSameDownStation(Station downStation) {
		return this.downStation.equals(downStation);
	}

	public boolean isSameUpDownStation(Section expectSection) {
		return isSameUpStation(expectSection.upStation) && isSameDownStation(expectSection.downStation);
	}

	public boolean isInUpDownStation(Station station) {
		return isSameUpStation(station) || isSameDownStation(station);
	}

	public void reSettingSection(Section expectSection) {
		if (isSameUpStation(expectSection.upStation)) {
			this.upStation = expectSection.downStation;
		}

		if (isSameDownStation(expectSection.downStation)) {
			this.downStation = expectSection.upStation;
		}

		this.distance.divideDistance(expectSection.distance);
	}

	public void plusDistance(Section removeSection) {
		this.distance.plusDistance(removeSection.distance);
	}
}
