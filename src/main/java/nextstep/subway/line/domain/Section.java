package nextstep.subway.line.domain;

import java.util.Objects;
import java.util.Optional;

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

	public Section(Line line, Station upStation, Station downStation, int distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = new Distance(distance);
	}

	public Section(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
		Station newUpStation = downLineStation.get().upStation();
		Station newDownStation = upLineStation.get().downStation();
		int newDistance = upLineStation.get().distance() + downLineStation.get().distance();

		this.line = line;
		this.upStation = newUpStation;
		this.downStation = newDownStation;
		this.distance = new Distance(newDistance);
	}

	public Long id() {
		return id;
	}

	public Line line() {
		return line;
	}

	public Station upStation() {
		return upStation;
	}

	public Station downStation() {
		return downStation;
	}

	public int distance() {
		return distance.value();
	}

	public void updateUpStation(Station station, int newDistance) {
		validateNotSameDownStation(station);
		this.upStation = station;
		this.distance = new Distance(distance.minus(newDistance));
	}

	private void validateNotSameDownStation(Station station) {
		if (downStation().isSameStation(station)) {
			throw new RuntimeException("상행역과 하행역이 같은 구간은 존재하지 않습니다.");
		}
	}

	public void updateDownStation(Station station, int newDistance) {
		validateNotSameUpStation(station);
		this.downStation = station;
		this.distance = new Distance(distance.minus(newDistance));
	}

	private void validateNotSameUpStation(Station station) {
		if (upStation().isSameStation(station)) {
			throw new RuntimeException("상행역과 하행역이 같은 구간은 존재하지 않습니다.");
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Section)) {
			return false;
		}
		Section section = (Section)object;
		return distance.equals(section.distance)
			&& Objects.equals(id, section.id)
			&& Objects.equals(upStation, section.upStation)
			&& Objects.equals(downStation, section.downStation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, upStation, downStation, distance);
	}
}
