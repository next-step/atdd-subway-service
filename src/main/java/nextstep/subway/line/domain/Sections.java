package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.SortNatural;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

  @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  @SortNatural
  private SortedSet<Section> lineSections = new TreeSet<>();

  public Sections() {
  }


  public void registerNewSection(Section newSection) {
    lineSections.add(newSection);
  }

  public Set<Station> getDistinctStations() {
    return lineSections.stream()
        .flatMap(Section::getUpAndDownStationStream)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void removeStation(Station 양재역) {

  }
}
