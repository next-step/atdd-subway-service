package nextstep.subway.line.domain;

import nextstep.subway.line.application.Distance;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

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

  public Section() {
  }

  public Section(Line line, Station upStation, Station downStation, Distance distance) {
    this.line = line;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public static Section of(Station upStation, Station downStation, Distance distance) {
    return new Section(null, upStation, downStation, distance);
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
}
