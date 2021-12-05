package nextstep.subway.line.domain.fixture;

import static nextstep.subway.line.domain.fixture.LineFixture.일호선;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.서울역;
import static nextstep.subway.station.domain.StationFixture.신림역;
import static nextstep.subway.station.domain.StationFixture.신용산역;
import static nextstep.subway.station.domain.StationFixture.신촌역;
import static nextstep.subway.station.domain.StationFixture.양평역;
import static nextstep.subway.station.domain.StationFixture.역삼역;
import static nextstep.subway.station.domain.StationFixture.용문역;
import static nextstep.subway.station.domain.StationFixture.용산역;

import nextstep.subway.line.domain.Section;

public class SectionFixture {

    public static Section 일호선_구간_강남역_신촌역() {
        return new Section(1L, 일호선(), 강남역(), 신촌역(), 10);
    }

    public static Section 일호선_구간_신촌역_역삼역() {
        return new Section(2L, 일호선(), 신촌역(), 역삼역(), 5);
    }

    public static Section 일호선_구간_역삼역_서울역() {
        return new Section(4L, 일호선(), 역삼역(), 서울역(), 4);
    }

    public static Section 일호선_존재하지않는_용문역_양평역() {
        return new Section(6L, 일호선(), 용문역(), 양평역(), 2);
    }

    public static Section 일호선_구간_상행추가_신림역_강남역() {
        return new Section(5L, 일호선(), 신림역(), 강남역(), 2);
    }

    public static Section 일호선_구간_중간추가_신용산역_서울역() {
        return new Section(7L, 일호선(), 신용산역(), 서울역(), 2);
    }

    public static Section 일호선_구간_하행추가_서울역_용산역() {
        return new Section(3L, 일호선(), 서울역(), 용산역(), 1);
    }
}
