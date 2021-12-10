package nextstep.subway.line.domain;

import java.util.Objects;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {
	public static final Section DUMMY_SECTION = new Section();

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

	public Section(Long id, Line line, Station upStation, Station downStation, Distance distance) {
		this.id = id;
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Section(Line line, Station upStation, Station downStation, Distance distance) {
		this(null, line, upStation, downStation, distance);
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

	public Long getUpStationId() {
		return getUpStation().getId();
	}

	public Long getDownStationId() {
		return getDownStation().getId();
	}

	public int getDistanceValue() {
		return getDistance().value();
	}

	public void updateUpStation(Section section) {
		if (distance.lessThan(section.distance)) {
			throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.upStation = section.getDownStation();
		this.distance.decrease(section.distance);
	}

	public void updateDownStation(Section section) {
		if (distance.lessThan(section.distance)) {
			throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.downStation = section.getUpStation();
		this.distance.decrease(section.distance);
	}

	public boolean equalsDownStation(Station station) {
		return this.downStation.equals(station);
	}

	public boolean equalsUpStation(Station station) {
		return this.upStation.equals(station);
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
