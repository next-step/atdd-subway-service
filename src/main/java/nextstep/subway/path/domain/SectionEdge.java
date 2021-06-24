package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fee;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {

  private final Section section;

  public SectionEdge(Section section) {
    this.section = section;
  }

  public Station getSourceVertex() {
    return section.getUpStation();
  }

  public Station getTargetVertex() {
    return section.getDownStation();
  }

  public int getEdgeWeight() {
    return section.getDistance()
                  .intValue();
  }

  public Fee getSectionLineAdditionalFee() {
    return section.getLine()
                  .getLineAdditionalFee();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SectionEdge that = (SectionEdge) o;
    return Objects.equals(section, that.section);
  }

  @Override
  public int hashCode() {
    return Objects.hash(section);
  }
}
