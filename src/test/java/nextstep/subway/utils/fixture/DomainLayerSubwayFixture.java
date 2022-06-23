package nextstep.subway.utils.fixture;

import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;

public class DomainLayerSubwayFixture {
    public final Station 강남역 = new Station(1L, "강남역");
    public final Station 양재역 = new Station(2L, "양재역");
    public final Station 교대역 = new Station(3L, "교대역");
    public final Station 남부터미널역 = new Station(4L, "남부터미널역");
    public final Station 여의도역 = new Station(5L, "여의도역");
    public final Station 샛강역 = new Station(6L, "샛강역");
    public final Line 신분당선 = new Line(1L, "신분당선", "red");
    public final Line 이호선 = new Line(2L, "이호선", "green");
    public final Line 삼호선 = new Line(3L, "삼호선", "orange");
    public final Line 구호선 = new Line(4L, "구호선", "brown");
    public final int 강남역_양재역_간_거리 = 20;
    public final int 교대역_강남역_간_거리 = 10;
    public final int 교대역_남부터미널역_간_거리 = 10;
    public final int 남부터미널역_양재역_간_거리 = 5;
    public final int 여의도역_샛강역_간_거리 = 15;
    public final Section 강남역_양재역_구간 = new Section(1L, 신분당선, 강남역, 양재역, 강남역_양재역_간_거리);
    public final Section 교대역_강남역_구간 = new Section(2L, 이호선, 교대역, 강남역, 교대역_강남역_간_거리);
    public final Section 교대역_남부터미널역_구간 = new Section(3L, 삼호선, 교대역, 남부터미널역, 교대역_남부터미널역_간_거리);
    public final Section 남부터미널역_양재역_구간 = new Section(4L, 삼호선, 남부터미널역, 양재역, 남부터미널역_양재역_간_거리);
    public final Section 여의도역_샛강역_구간 = new Section(5L, 구호선, 여의도역, 샛강역, 여의도역_샛강역_간_거리);
}
