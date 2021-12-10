package nextstep.subway.line.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
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

	private Section(Long id, Line line, Station upStation, Station downStation, Distance distance) {
		this.id = id;
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static Section of(Long id, Line line, Station upStation, Station downStation, Distance distance) {
		return new Section(id, line, upStation, downStation, distance);
	}

	public static Section of(Long id, Station upStation, Station downStation, int distance) {
		return new Section(id, null, upStation, downStation, Distance.of(distance));
	}

	public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
		return new Section(null, line, upStation, downStation, distance);
	}

	public void updateUpStation(Station station, Distance newDistance) {
		if (this.distance.lessThanAndEqual(newDistance)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.upStation = station;
		this.distance = this.distance.minus(newDistance);
	}

	public void updateDownStation(Station station, Distance newDistance) {
		if (this.distance.lessThanAndEqual(newDistance)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
		}
		this.downStation = station;
		this.distance = this.distance.minus(newDistance);
	}

	public static Section combine(Section forward, Section backward) {
		Distance newDistance = forward.getDistance().plus(backward.getDistance());
		return Section.of(forward.line, forward.upStation, backward.downStation, newDistance);
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Section section = (Section)o;

		return id.equals(section.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
