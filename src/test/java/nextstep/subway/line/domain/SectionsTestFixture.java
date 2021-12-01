package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.Transient;

public class SectionsTestFixture {
    private SectionsTestFixture() {
        throw new UnsupportedOperationException();
    }

    @Transient
    public static Line 이호선 = new Line("이호선", "bg-green-600");
    @Transient
    public static Station 강남역 = new Station("강남역");
    @Transient
    public static Station 역삼역 = new Station("역삼역");
    @Transient
    public static Station 교대역 = new Station("교대역");
    @Transient
    public static Section 강남역_역삼역_구간 = new Section(이호선, 강남역, 역삼역, 10);
    @Transient
    public static Section 역삼역_교대역_구간 = new Section(이호선, 역삼역, 교대역, 10);
}
