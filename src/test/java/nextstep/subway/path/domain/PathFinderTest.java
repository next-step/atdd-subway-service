package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionException;
import nextstep.subway.line.domain.line.Line;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.line.domain.section.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {

    private Station 홍대역 = new Station("홍대역");
    private Station 선릉역 = new Station("선릉역");
    private Station 역삼역 = new Station("역삼역");
    private Station 강남역 = new Station("강남역");
    private Station 판교역 = new Station("판교역");
    private Station 수지역 = new Station("수지역");
    private Station 광교역 = new Station("광교역");

    private Line 신분당선_강남_판교_수지_광교 = new Line("신분당선", "red", 강남역, 광교역, 20);
    private Line 이호선_강남_역삼_선릉 = new Line("2호선", "red", 강남역, 선릉역, 30);

    private PathFinder pathFinder;
    @BeforeEach
    void set_신분당선_강남_판교_수지_광교() {
        신분당선_강남_판교_수지_광교.addSection(강남역, 판교역, 4);
        신분당선_강남_판교_수지_광교.addSection(판교역, 수지역, 7);
        이호선_강남_역삼_선릉.addSection(강남역, 역삼역, 10);

        pathFinder = new PathFinder(Stream.concat(신분당선_강남_판교_수지_광교.getSections().stream(), 이호선_강남_역삼_선릉.getSections().stream())
                .collect(Collectors.toList()));
    }

    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (선릉 -> 역삼 -> 강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_노선에_미등록_역_조회_요청() {
        assertThatThrownBy(() -> {
            pathFinder.getDijkstraShortestPath(강남역, 홍대역);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("지하철 노선 역 조회 출발지와 도착지 동일 오류 (선릉 -> 역삼 -> 강남 -> 판교 -> 수지 -> 광교)")
    public void 지하철_노선에_조회_요청_출발역_도착역_동일_오류() {
        assertThatThrownBy(() -> {
            pathFinder.getDijkstraShortestPath(강남역, 강남역);
        }).isInstanceOf(SectionException.class);
    }

    @Test
    @DisplayName("지하철 노선에 등록된 역 기준으로 순방향 조회 (선릉 --(20)> [ 역삼 --(10)> 강남 --(4)> 판교 --(7)> 수지 ] --(9)> 광교)")
    public void 지하철_노선에_등록_역_조회_요청() {
        List<Station> actual = pathFinder.getDijkstraShortestPath(역삼역, 수지역);
        int actualDistance = pathFinder.getSumLineStationsDistance(역삼역, 수지역);

        assertAll(
                () -> assertThat(actual).containsExactly(역삼역, 강남역, 판교역, 수지역),
                () -> assertThat(actualDistance).isEqualTo(21)
        );

    }

    @Test
    @DisplayName("지하철 노선에 등록된 역 기준으로 역방향 조회 (선릉 --(20)> [ 역삼 <--(10) <-- 강남 <(4)-- 판교 <(7)-- 수지 <(9)-- 광교 ] )")
    public void 지하철_노선에_등록_역_조회_요청_역방향() {
        List<Station> actual = pathFinder.getDijkstraShortestPath(광교역, 역삼역);
        int actualDistance = pathFinder.getSumLineStationsDistance(광교역, 역삼역);

        assertAll(
                () -> assertThat(actual).containsExactly(광교역, 수지역, 판교역, 강남역, 역삼역),
                () -> assertThat(actualDistance).isEqualTo(30)
        );
    }

}