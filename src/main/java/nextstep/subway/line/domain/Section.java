package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.path.domain.fare.Money;
import nextstep.subway.station.domain.Station;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

	public Section(Line line, Station upStation, Station downStation, int distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = new Distance(distance);
	}

	public static Section of(Section upSection, Section downSection) {
		return new Section(upSection.getLine(),
			downSection.getUpStation(),
			upSection.getDownStation(),
			upSection.distance() + downSection.distance());
	}

	public void updateUpStation(Section target) {
		this.distance.update(target.distance());
		this.upStation = target.getDownStation();
	}

	public void updateDownStation(Section target) {
		this.distance.update(target.distance());
		this.downStation = target.getUpStation();
	}

	public List<Station> getStations() {
		return Arrays.asList(upStation, downStation);
	}

	public boolean contains(Station target) {
		return getStations().contains(target);
	}

	public boolean isUpStation(Station station) {
		return this.upStation.equals(station);
	}

	public boolean isDownStation(Station station) {
		return this.downStation.equals(station);
	}

	public int distance() {
		return this.distance.getDistance();
	}

	public Money fare() {
		return this.line.getFare();
	}
}
