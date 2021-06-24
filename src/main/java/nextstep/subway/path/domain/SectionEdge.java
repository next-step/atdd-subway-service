package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {

  private final Section section;

  public SectionEdge(Section section) {
    this.section = section;
  }

  public Long getSourceVertex() {
    return section.getUpStation().getId();
  }

  public Long getTargetVertex() {
    return section.getDownStation().getId();
  }

  public int getEdgeWeight() {
    return section.getDistance().intValue();
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
