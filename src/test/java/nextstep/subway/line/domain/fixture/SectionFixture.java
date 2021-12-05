package nextstep.subway.line.domain.fixture;

import static nextstep.subway.line.domain.fixture.LineFixture.일호선;
import static nextstep.subway.station.domain.StationFixture.*;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.StationFixture;

public class SectionFixture {

    public static Section 일호선_구간_강남역_신촌역() {
        return new Section(1L, 일호선(), 강남역(), 신촌역(), 10);
    }

    public static Section 일호선_구간_신촌역_역삼역() {
        return new Section(2L, 일호선(), 신촌역(), 역삼역(), 5);
    }

    public static Section 일호선_구간_역삼역_서울역() {
        return new Section(3L, 일호선(), 역삼역(), 서울역(), 4);
    }

    public static Section 일호선_존재하지않는_용문역_양평역() {
        return new Section(4L, 일호선(), 용문역(), 양평역(), 2);
    }

    public static Section 일호선_구간_상행추가_신림역_강남역() {
        return new Section(5L, 일호선(), 신림역(), 강남역(), 2);
    }

    public static Section 일호선_구간_중간추가_신용산역_서울역() {
        return new Section(5L, 일호선(), 신용산역(), 서울역(), 2);
    }

    public static Section 일호선_구간_하행추가_서울역_용산역() {
        return new Section(6L, 일호선(), 서울역(), 용산역(), 1);
    }

    public static Section 이호선_구간_합정역_홍대입구역() {
        return new Section(7L, 일호선(), 합정역(), 홍대입구역(), 10);
    }

    public static Section 이호선_구간_홍대입구역_신촌역() {
        return new Section(8L, 일호선(), 홍대입구역(), 신촌역(), 5);
    }

    public static Section 이호선_구간_신촌역_을지로3가역() {
        return new Section(9L, 일호선(),신촌역(), 을지로3가역(),  5);
    }

    public static Section 삼호선_구간_안국역_종로3가역() {
        return new Section(10L, 일호선(), 합정역(), 홍대입구역(), 2);
    }

    public static Section 삼호선_구간_종로3가역_을지로3가역() {
        return new Section(11L, 일호선(), 종로3가역(), 을지로3가역(), 2);
    }

    public static Section 삼호선_구간_을지로3가역_충무로역() {
        return new Section(12L, 일호선(), 을지로3가역(), 충무로역(), 1);
    }
}
