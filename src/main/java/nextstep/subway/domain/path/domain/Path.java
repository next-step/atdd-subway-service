package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;

import java.util.List;

public interface Path {

    int getWeight(final Long source, final Long target);
    void createEdge(final List<Line> lines);
    void createVertex(final List<Line> lines);
    List<Long> getVertex(final Long source, final Long target);
}
