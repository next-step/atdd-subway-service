package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class SectionTestFixture {

    public static Section createSection(Line line, Station upStation, Station downStation, int distance) {
        Section section = Section.of(line, upStation, downStation, distance);
        line.addSection(section);
        return section;
    }
}
