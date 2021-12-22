package nextstep.subway.line.domain.fixture;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.fixture.StationFixture;

public class SectionFixture {
    // 신분당선
    public static final Section 강남역_양재역 = new Section(LineFixture.신분당선, StationFixture.강남역, StationFixture.양재역, 2);
    public static final Section 양재역_양재시민의숲역 = new Section(LineFixture.신분당선, StationFixture.양재역, StationFixture.양재시민의숲역, 4);
    public static final Section 양재시민의숲역_청계산입구역 = new Section(LineFixture.신분당선, StationFixture.양재시민의숲역, StationFixture.청계산입구역, 4);
    public static final Section 청계산입구역_판교역 = new Section(LineFixture.신분당선, StationFixture.청계산입구역, StationFixture.판교역, 3);
    public static final Section 판교역_정자역 = new Section(LineFixture.신분당선, StationFixture.판교역, StationFixture.정자역, 6);
    public static final Section 정자역_미금역 = new Section(LineFixture.신분당선, StationFixture.정자역, StationFixture.미금역, 5);
    public static final Section 미금역_동천역 = new Section(LineFixture.신분당선, StationFixture.미금역, StationFixture.동천역, 2);
    public static final Section 동천역_광교역 = new Section(LineFixture.신분당선, StationFixture.동천역, StationFixture.광교역, 3);
}
