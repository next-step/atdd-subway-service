package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Station 클래스")
public class SectionTest {
    public static Section 섹션_생성(final Line line, final Station upStation, final Station downStation,
                                final int distance) {
        return new Section(line, upStation, downStation, new Distance(distance));
    }

    public static Sections 빈_섹션_생성() {
        return new Sections();
    }
}
