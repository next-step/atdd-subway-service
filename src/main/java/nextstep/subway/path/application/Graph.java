package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;

public interface Graph {

    Path getPath(Sections sections);
}
