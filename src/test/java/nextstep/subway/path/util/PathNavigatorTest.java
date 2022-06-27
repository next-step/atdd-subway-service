package nextstep.subway.path.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PathNavigatorTest 클래스")
public class PathNavigatorTest {

    private List<Line> 역_목록 = new ArrayList<>();
    private Line 일호선;
    private Line 구호선;
    private Line 오호선;
    private Station 노량진역 = 역_생성("노량진역");
    private Station 용산역 = 역_생성("용산역");
    private Station 샛강역 = 역_생성("샛강역");
    private Station 여의도역 = 역_생성("여의도역");
    private Station 신길역 = 역_생성("신길역");
    private Station 여의나루역 = 역_생성("여의나루역");

    @BeforeEach
    void setUp() {
        일호선 = 라인_생성("일호선", "bg-blue-600", 200, 노량진역, 용산역, 5);
        구호선 = 라인_생성("구호선", "bg-yellow-600", 300, 노량진역, 여의도역, 10);
        오호선 = 라인_생성("오호선", "bg-purple-600", 300, 신길역, 여의나루역, 12);
        구간_추가(구호선, 노량진역, 샛강역, 5);
        구간_추가(오호선, 여의도역, 여의나루역, 3);
        역_목록.add(일호선);
        역_목록.add(구호선);
        역_목록.add(오호선);
    }

    @DisplayName("getPath 메서드는 최단경로 역목록과 거리를 반환한다.")
    @Test
    void getPathTest() {
        //given
        PathNavigator pathNavigator = PathNavigator.of(역_목록);

        //when
        Path 최단_경로 = pathNavigator.getPath(용산역, 여의나루역);

        //then
        최단_경로_거리_확인(최단_경로, 18);
        최단_경로_역_목록_확인(최단_경로, 용산역, 노량진역, 샛강역, 여의도역, 여의나루역);

    }

    private void 최단_경로_거리_확인(Path path, int distance) {
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    private void 최단_경로_역_목록_확인(Path path, Station... stations) {
        assertThat(path.getStations()).containsExactly(stations);
    }

    private void 구간_추가(Line line, Station upStation, Station downStation, int distance) {
        line.addSection(upStation, downStation, distance);
    }

    private Station 역_생성(String name) {
        return new Station(name);
    }

    private Line 라인_생성(String name, String color, int surcharge, Station upStation, Station downStation, int distance) {
        return new Line(name, color, surcharge, upStation, downStation, distance);
    }
}
