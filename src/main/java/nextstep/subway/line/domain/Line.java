package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Line extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  private String color;

  private int surcharge = 0;

  @Embedded
  private Sections sections = new Sections();

  protected Line() {}

  public Line(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public Line(Long id, String name, String color, Section section) {
    this.id = id;
    this.name = name;
    this.color = color;
    addSection(section);
  }

  public Line(String name, String color, Station upStation, Station downStation, Distance distance) {
    this(name, color);
    sections.add(new Section(this, upStation, downStation, distance));
  }

  public Line(String name, String color, int surcharge, Section section) {
    this.name = name;
    this.color = color;
    this.surcharge = surcharge;
    addSection(section);
  }

  public void update(Line line) {
    this.name = line.getName();
    this.color = line.getColor();
  }

  public void addSection(Section newSection) {
    newSection.addLine(this);
    sections.add(newSection);
  }

  public void addSurcharge(int surcharge) {
    this.surcharge = surcharge;
  }

  public boolean hasSection(List<Station> stations) {
    for (int i = 0; i < stations.size() - 1; i++) {
      Station currentStation = stations.get(i);
      Station nextStation = stations.get(i + 1);
      if (hasStationsFromSection(currentStation, nextStation)) return true;
    }

    return false;
  }

  private boolean hasStationsFromSection(Station... stations) {
    return sections.containsAll(new ArrayList<>(Arrays.asList(stations)));
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

  public int getSurcharge() {
    return surcharge;
  }

  public List<Section> getSections() {
    return sections.getOrderedSections();
  }

  public List<Station> getStations() {
    return sections.getOrderedStations();
  }
}
