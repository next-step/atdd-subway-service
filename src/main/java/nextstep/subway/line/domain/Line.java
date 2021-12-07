package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.application.Distance;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  private String color;

  @Embedded
  private Sections sections = new Sections();

  public Line() {
  }

  public Line(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public Line(Long id, String name, String color, Section section) {
    this.id = id;
    this.name = name;
    this.color = color;
    sections.add(section);
  }

  public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
    this.name = name;
    this.color = color;
    sections.add(new Section(this, upStation, downStation, distance));
  }

  public void update(Line line) {
    this.name = line.getName();
    this.color = line.getColor();
  }

  public void addSection(Section newSection) {
    newSection.addLine(this);
    sections.add(newSection);
  }

  public void removeStation(Station station) {
    sections.remove(station);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public Sections getSections() {
    return sections;
  }

  public List<Station> getStations() {
    return sections.getOrderedStations().getStations();
  }
}
