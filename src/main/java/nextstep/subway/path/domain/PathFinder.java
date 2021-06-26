package nextstep.subway.path.domain;

import java.util.Collection;

public interface PathFinder {

    Collection findPaths();

    int measureDistance();
}
