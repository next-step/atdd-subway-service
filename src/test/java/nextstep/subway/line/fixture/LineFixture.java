package nextstep.subway.line.fixture;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

import static nextstep.subway.fare.domain.FaresByDistance.BASIC_FARE;
import static nextstep.subway.station.fixture.StationFixture.*;

public class LineFixture {
    /*
    # 지하철 노선도 #
                            (2호선)                          (1호선)
     (5호선)  양평역 - 10 - 영등포구청역 - 5 - 영등포시장역 - 10 - 신길역 - 10 여의도 역  (5호선)
                              ㅣ                               ㅣ
                              10                               5
                              ㅣ                               ㅣ
                             당산역                           영등포역
                             (2호선)                          (1호선)

     # 노선별 추가요금 #
     - 5호선 : 500원
     - 2호선 : 200원
     - 1호선 : 100원
    */

    public static Line 오호선 = new Line(1L, "오호선", "bg-red-600", 양평역, 영등포구청역, new Distance(10), new Fare(500));
    public static Line 이호선 = new Line(2L, "이호선", "bg-red-600", 영등포구청역, 당산역, new Distance(10), new Fare(200));
    public static Line 일호선 = new Line(3L, "일호선", "bg-red-600", 신길역, 영등포역, new Distance(5), new Fare(100));
    public static Line 신분당선 = new Line(4L, "신분당선", "bg-red-600", 야탑역, 모란역, new Distance(5), BASIC_FARE);
}
