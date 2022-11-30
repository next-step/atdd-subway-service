package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class FromUpperSectionAdder implements SectionAdder {
    @Override
    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        line.getSections().stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));

        line.addSection(upStation, downStation, distance);
    }
}
