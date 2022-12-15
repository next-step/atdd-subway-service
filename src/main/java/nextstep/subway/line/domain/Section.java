package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import nextstep.subway.common.domain.Fare;
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

	private Section(Line line, Station upStation, Station downStation, Distance distance) {
		validate(upStation, downStation, distance);
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	private void validate(Station upStation, Station downStation, Distance distance) {
		Assert.notNull(upStation, "상행역은 필수입니다.");
		Assert.notNull(downStation, "하행역은 필수입니다.");
		Assert.notNull(distance, "거리는 필수입니다.");
		Assert.isTrue(!upStation.equals(downStation), "상행역과 하행역은 같을 수 없습니다.");
	}

	public static Section of(Line line, Station upStation, Station downStation, Distance distance) {
		return new Section(line, upStation, downStation, distance);
	}

	public static Section of(Station upStation, Station downStation, Distance distance) {
		return new Section(null, upStation, downStation, distance);
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
		return distance.value();
	}

	public void updateLine(Line line) {
		this.line = line;
	}

	public Fare extraFare() {
		return line.extraFare();
	}

	public boolean isSameUpStation(Section newSection) {
		return isSameStation(this.upStation, newSection.upStation);
	}

	public boolean isSameDownStation(Section newSection) {
		return isSameStation(this.downStation, newSection.downStation);
	}

	private boolean isSameStation(Station station, Station newStation) {
		return station.equals(newStation);
	}

	public void replaceUpStation(Section newSection) {
		this.upStation = newSection.downStation;
		this.distance = this.distance.subtract(newSection.distance);
	}

	public void replaceDownStation(Section newSection) {
		this.downStation = newSection.upStation;
		this.distance = this.distance.subtract(newSection.distance);
	}

	public void extend(Section sectionByUpStation) {
		this.downStation = sectionByUpStation.downStation;
		this.distance = this.distance.add(sectionByUpStation.distance);
	}

	public static Section removeMiddleSection(Section sectionByUpStation, Section sectionByDownStation) {
		sectionByDownStation.extend(sectionByUpStation);
		return sectionToRemove(sectionByUpStation);
	}

	private static Section sectionToRemove(Section sectionByUpStation) {
		return sectionByUpStation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Section section = (Section)o;
		if (!Objects.equals(upStation, section.upStation))
			return false;
		if (!Objects.equals(downStation, section.downStation))
			return false;
		return Objects.equals(distance, section.distance);
	}

	@Override
	public int hashCode() {
		int result = upStation != null ? upStation.hashCode() : 0;
		result = 31 * result + (downStation != null ? downStation.hashCode() : 0);
		result = 31 * result + (distance != null ? distance.hashCode() : 0);
		return result;
	}
}
