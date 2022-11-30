package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public interface SectionAdder {
    void addSection(Line line, Station upperStation, Station downStation, int distance);
}
