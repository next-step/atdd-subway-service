package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PathFareTest {
    @InjectMocks
    private PathFare pathFare;

    Line 이호선;
    Line 사호선;

    Station 서울대입구역;
    Station 낙성대역;
    Station 사당역;
    Station 방배역;
    Station 교대역;
    Station 이수역;
    Station 동작역;

    Section 서울대입구역_낙성대역;
    Section 낙성대역_사당역;
    Section 사당역_방배역;
    Section 방배역_교대역;
    Section 교대역_이수역;
    Section 이수역_동작역;

    private List<Section> 단일_지하철_구간;
    private List<DefaultWeightedEdge> 단일_그래프_간선;

    private List<Section> 다중_지하철_구간;
    private List<DefaultWeightedEdge> 다중_그래프_간선;

    @BeforeEach
    void beforeEach() {
        이호선 = new Line(1L, "이호선", "green", 0);
        사호선 = new Line(2L, "사호선", "sky", 1000);

        서울대입구역 = new Station(1L, "서울대입구역");
        낙성대역 = new Station(2L, "낙성대역");
        사당역 = new Station(3L, "사당역");
        방배역 = new Station(4L, "방배역");
        교대역 = new Station(5L, "교대역");
        이수역 = new Station(6L, "이수역");
        동작역 = new Station(7L, "동작역");

        서울대입구역_낙성대역 = new Section(이호선, 서울대입구역, 낙성대역, 15);
        낙성대역_사당역 = new Section(이호선, 낙성대역, 사당역, 15);
        사당역_방배역 = new Section(이호선, 사당역, 방배역, 16);
        방배역_교대역 = new Section(이호선, 방배역, 교대역, 15);
        교대역_이수역 = new Section(사호선, 교대역, 이수역, 15);
        이수역_동작역 = new Section(사호선, 이수역, 동작역, 16);

        단일_그래프_간선 = new LinkedList<>();
        단일_지하철_구간 = new LinkedList<>();
        다중_그래프_간선 = new LinkedList<>();
        다중_지하철_구간 = new LinkedList<>();
    }

    // 기본운임
    @Test
    void create_basic_fare() {
        // given
        기본운임_구간_생성();

        // when
        int fare = pathFare.calculateFare(단일_그래프_간선, 단일_지하철_구간);

        // then
        assertThat(fare).isEqualTo(2050);
    }

    // 추가요금
    @Test
    void create_surcharge_fare() {
        // given
        추가요금_구간_생성();

        // when
        int fare = pathFare.calculateFare(다중_그래프_간선, 다중_지하철_구간);

        // then
        assertThat(fare).isEqualTo(3050);
    }

    // 환승 - 추가요금
    @Test
    void create_surcharge_fare_() {
        // given
        환승요금_구간_생성();

        // when
        int fare = pathFare.calculateFare(다중_그래프_간선, 다중_지하철_구간);

        // then
        assertThat(fare).isEqualTo(3650);
    }

    void 기본운임_구간_생성() {
        단일_지하철_구간.add(서울대입구역_낙성대역);
        단일_지하철_구간.add(낙성대역_사당역);
        단일_지하철_구간.add(사당역_방배역);

        이호선.addLineStation(서울대입구역_낙성대역);
        이호선.addLineStation(낙성대역_사당역);
        이호선.addLineStation(사당역_방배역);

        단일_그래프_간선.addAll(new PathMap().createMap(Arrays.asList(이호선)).edgeSet());
    }

    void 추가요금_구간_생성() {
        다중_지하철_구간.add(방배역_교대역);
        다중_지하철_구간.add(교대역_이수역);
        다중_지하철_구간.add(이수역_동작역);

        사호선.addLineStation(방배역_교대역);
        사호선.addLineStation(교대역_이수역);
        사호선.addLineStation(이수역_동작역);

        다중_그래프_간선.addAll(new PathMap().createMap(Arrays.asList(사호선)).edgeSet());
    }

    void 환승요금_구간_생성() {
        기본운임_구간_생성();
        추가요금_구간_생성();
        다중_지하철_구간.addAll(단일_지하철_구간);
        다중_그래프_간선.addAll(단일_그래프_간선);
    }
}
