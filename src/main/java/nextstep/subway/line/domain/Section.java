package nextstep.subway.line.domain;

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
public class Section {
	private static final int DISTANCE_LESS_THAN_ZERO_NOT_ALLOWED = 0;

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

	protected Section() {
	}

	public Section(Line line, Station upStation, Station downStation, int distance) {
		validateDistance(distance);
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	private void validateDistance(int distance) {
		if (distance <= DISTANCE_LESS_THAN_ZERO_NOT_ALLOWED) {
			throw new RuntimeException("구간의 간격은 0을 초과하는 거리여야 합니다.");
		}
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
		return distance;
	}

	public void updateUpStation(Station station, int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.upStation = station;
		this.distance -= newDistance;
	}

	public void updateDownStation(Station station, int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.downStation = station;
		this.distance -= newDistance;
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
		return distance == section.distance
			&& Objects.equals(id, section.id)
			&& Objects.equals(upStation, section.upStation)
			&& Objects.equals(downStation, section.downStation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, upStation, downStation, distance);
	}
}
