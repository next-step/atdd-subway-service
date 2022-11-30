package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public interface Find {

    void isExistPath(Station source, Station target) throws IllegalArgumentException;

    List<Station> getVertexList(Station source, Station target);

    int getWeight(Station source, Station target);

    void setEdgeWeight(Section section, int weight);

    void addVertex(Station station);
}
