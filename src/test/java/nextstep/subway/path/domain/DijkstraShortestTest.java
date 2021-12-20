package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DijkstraShortestTest {

    private Station 교대역 = null;
    private Station 강남역 = null;
    private Station 남부터미널역 = null;
    private Station 양재역 = null;
    private Sections 첫번째구간 = null;
    private Sections 두번째구간 = null;
    private Sections 세번째구간 = null;
    private Section 이호선구간 = null;
    private Section 신분당선구간 = null;
    private Section 삼호선구간1 = null;
    private Section 삼호선구간2 = null;
    private Station 사당역 = null;
    private Station 이수역 = null;
    private Sections 다른구간 = null;
    private Line 이호선 = null;
    private Line 삼호선 = null;
    private Line 신분당선 = null;
    private Line 다른노선 = null;
    private List<Line> 전라인 = null;

    @BeforeEach
    void setUp() {
        교대역 = Station.from("교대역");
        강남역 = Station.from("강남역");
        남부터미널역 = Station.from("남부터미널역");
        양재역 = Station.from("양재역");

        이호선구간 = Section.of(교대역, 강남역, Distance.of(10));
        신분당선구간 = Section.of(강남역, 양재역, Distance.of(10));
        삼호선구간1 = Section.of(교대역, 남부터미널역, Distance.of(3));
        삼호선구간2 = Section.of(남부터미널역, 양재역, Distance.of(5));

        첫번째구간 = Sections.from(이호선구간);
        두번째구간 = Sections.from(신분당선구간);
        세번째구간 = Sections.from(Lists.newArrayList(삼호선구간1, 삼호선구간2));

        이호선 = Line.of("이호선", "blue", 첫번째구간, Fare.from(900));
        삼호선 = Line.of("삼호선", "blue", 세번째구간, Fare.from(900));
        신분당선 = Line.of("신분당선", "blue", 두번째구간, Fare.from(900));

        이호선구간.addLine(이호선);
        신분당선구간.addLine(신분당선);
        삼호선구간1.addLine(삼호선);
        삼호선구간2.addLine(삼호선);

        사당역 = Station.from("사당역");
        이수역 = Station.from("이수역");
        다른구간 = Sections.from(Section.of(사당역, 이수역, Distance.of(1)));
        다른노선 = Line.of("이호선", "blue", 다른구간, Fare.from(900));

        전라인 = Lists.newArrayList(
                이호선,
                삼호선,
                신분당선,
                다른노선);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("최적 경로를 찾는다.")
    @Test
    void pathFinder() {
        // when
        DijkstraShortest dijkstraShortest = new DijkstraShortest();
        Path path = dijkstraShortest.getShortestPath(전라인, 교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).hasSize(3),
                () -> assertThat(path.getDistance()).isEqualTo(Distance.of(8)),
                () -> assertThat(path.getMaxExtraFare()).isEqualTo(Fare.from(new BigDecimal("900")))
        );
    }

    @DisplayName("경로를 찾는 출발역과 도착역이 같은 경우 에러 처리를 한다.")
    @Test
    void pathFinder_예외1() {
        // when
        DijkstraShortest dijkstraShortest = new DijkstraShortest();

        // then
        assertThatThrownBy(() -> dijkstraShortest.getShortestPath(전라인, 교대역, 교대역))
                .isInstanceOf(PathSameException.class)
                .hasMessage("출발역과 도착역이 같은 경우 경로 조회 할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러 처리를 한다.")
    @Test
    void pathFinder_예외2() {
        // when
        DijkstraShortest dijkstraShortest = new DijkstraShortest();
        assertThatThrownBy(() -> dijkstraShortest.getShortestPath(전라인, 교대역, 이수역))
                .isInstanceOf(PathDisconnectedException.class)
                .hasMessage("요청한 경로는 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리를 한다.")
    @Test
    void pathFinder_예외3() {
        // when
        DijkstraShortest dijkstraShortest = new DijkstraShortest();
        assertThatThrownBy(() -> dijkstraShortest.getShortestPath(전라인, 교대역, Station.from("존재하지않는역")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 정보는 존재하지 않습니다.");
    }
}
