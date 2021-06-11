package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.hibernate.annotations.SortNatural;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


public class Sections {

  public Sections() {
  }


  public void registerNewSection(Section first) {

  }

  public List<Station> getDistinctStations() {
    return Collections.emptyList();
  }

  public void removeStation(Station 양재역) {

  }
}
