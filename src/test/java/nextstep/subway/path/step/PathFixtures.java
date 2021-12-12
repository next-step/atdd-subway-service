package nextstep.subway.path.step;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.application.PathSearch;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.infrastructure.path.PathSearchImpl;
import nextstep.subway.member.domain.Age;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;

public class PathFixtures {

    private PathSearch pathSearch;
    private Lines lines;

    public static Station 교대 = new Station(1L, "교대");
    public static Station 강남 = new Station(2L, "강남");
    public static Station 양재 = new Station(3L, "양재");
    public static Station 남부터미널 = new Station(4L, "남부터미널");
    public static Station 천호 = new Station(5L, "천호");
    public static Station 강동구청 = new Station(6L, "강동구청");
    public static Station 없는역 = new Station(7L, "없는역");
    public static Money 이호선추가요금 = Money.won(900);
    public static Money 삼호선추가요금 = Money.won(500);

    @BeforeEach
    void setup() {
        lines = new Lines(전체구간());
        pathSearch = new PathSearchImpl();
    }

    protected Fare 경로조회_됨(Station source, Station target, int age) {
        Path path = lines.toPath(source, target, pathSearch);
        PathResult pathSearchResult = path.getShortestPath();
        return Fare.of(pathSearchResult, Age.of(age));
    }

    public static List<Line> 전체구간() {
        Line 이호선 = new Line("이호선", "red", 이호선추가요금.getMoney().intValue());
        Line 삼호선 = new Line("삼호선", "red", 삼호선추가요금.getMoney().intValue());
        Line 신분당선 = new Line("신분당선", "red");
        Line 팔호선 = new Line("팔호선", "red");

        신분당선.addSection(강남, 양재, 10);
        이호선.addSection(교대, 강남, 10);
        삼호선.addSection(교대, 남부터미널, 2);
        삼호선.addSection(남부터미널, 양재, 3);
        팔호선.addSection(강동구청, 천호, 55);

        return Arrays.asList(이호선, 삼호선, 신분당선, 팔호선);
    }

}
