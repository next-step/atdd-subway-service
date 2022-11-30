package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class FromDownSectionAdder implements SectionAdder {
    @Override
    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        line.getSections().stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));

        line.addSection(upStation, downStation, distance);
    }
}
