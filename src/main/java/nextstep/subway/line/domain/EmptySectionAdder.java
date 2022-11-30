package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class EmptySectionAdder implements SectionAdder {
    @Override
    public void addSection(Line line, Station upperStation, Station downStation, int distance) {
        line.addSection(upperStation, downStation, distance);
    }
}
