package nextstep.subway.line.domain.dummy;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.dummy.StationDummy;

public class SectionDummy {
    public static final Section 강남역_양재역 = new Section(LineDummy.신분당선, StationDummy.강남역, StationDummy.양재역, 2);
    public static final Section 양재역_양재시민의숲역 = new Section(LineDummy.신분당선, StationDummy.양재역, StationDummy.양재시민의숲역, 4);
    public static final Section 양재시민의숲역_청계산입구역 = new Section(LineDummy.신분당선, StationDummy.양재시민의숲역, StationDummy.청계산입구역, 4);
    public static final Section 청계산입구역_판교역 = new Section(LineDummy.신분당선, StationDummy.청계산입구역, StationDummy.판교역, 3);
    public static final Section 판교역_정자역 = new Section(LineDummy.신분당선, StationDummy.판교역, StationDummy.정자역, 6);
    public static final Section 정자역_미금역 = new Section(LineDummy.신분당선, StationDummy.정자역, StationDummy.미금역, 5);
    public static final Section 미금역_동천역 = new Section(LineDummy.신분당선, StationDummy.미금역, StationDummy.동천역, 2);
    public static final Section 동천역_광교역 = new Section(LineDummy.신분당선, StationDummy.동천역, StationDummy.광교역, 3);
}
