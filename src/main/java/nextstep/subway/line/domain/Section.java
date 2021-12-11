package nextstep.subway.line.domain;

import nextstep.subway.line.application.Distance;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Section {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "line_id")
  private Line line;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  @Embedded
  private Distance distance;

  protected Section() {
  }

  public Section(Line line, Station upStation, Station downStation, Distance distance) {
    this.line = line;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public Section(Long id, Line line, Station upStation, Station downStation, Distance distance) {
    this.id = id;
    this.line = line;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public static Section of(Station upStation, Station downStation, Distance distance) {
    return new Section(null, upStation, downStation, distance);
  }

  public static Section of(Long id, Station upStation, Station downStation, Distance distance) {
    return new Section(id, null, upStation, downStation, distance);
  }

  public void addLine(Line line) {
    this.line = line;
  }

  public void updateUpSideSection(Section newSection) {
    upStation = newSection.downStation;
    distance = distance.minus(newSection.distance);
  }

  public void updateDownSideSection(Section newSection) {
    downStation = newSection.upStation;
    distance = distance.minus(newSection.distance);
  }

  public void updateDownStation(Station station, Distance newDistance) {
    this.downStation = station;
    distance = distance.plus(newDistance);
  }

  public boolean isMatch(Section section) {
    return upStation.equals(section.upStation) && downStation.equals(section.downStation);
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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Section section = (Section) o;
    return Objects.equals(id, section.id) && Objects.equals(line, section.line);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, line);
  }

  @Override
  public String toString() {
    return "Section{" +
            "id=" + id +
            ", line=" + line +
            ", upStation=" + upStation +
            ", downStation=" + downStation +
            ", distance=" + distance +
            '}';
  }
}
