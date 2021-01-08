package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	private int distance;

	public Section(Line line, Station upStation, Station downStation, int distance) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public void updateUpStation(Section target) {
		this.updateUpStation(target.getDownStation(), getDistance());
	}

	public void updateDownStation(Section target) {
		this.updateDownStation(target.getUpStation(), target.getDistance());
	}

	public void updateUpStation(Station station, int newDistance) {
		validateDistance(newDistance);
		this.upStation = station;
		this.distance -= newDistance;
	}

	public void updateDownStation(Station station, int newDistance) {
		validateDistance(newDistance);
		this.downStation = station;
		this.distance -= newDistance;
	}

	public List<Station> getStations() {
		return Arrays.asList(upStation, downStation);
	}

	public boolean contains(Station target) {
		return getStations().contains(target);
	}

	public boolean isUpStation(Section target) {
		return this.upStation.equals(target.getUpStation());
	}

	public boolean isDownStation(Section target) {
		return this.downStation.equals(target.getDownStation());
	}

	private void validateDistance(int newDistance) {
		if (this.distance <= newDistance) {
			throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
	}
}
