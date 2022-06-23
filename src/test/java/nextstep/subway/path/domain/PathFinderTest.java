package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.AgeGroup;
import nextstep.subway.path.dto.PathStation;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static nextstep.subway.line.domain.LineTest.노선_생성;
import static nextstep.subway.path.application.PathServiceTest.라인_목록_생성;
import static nextstep.subway.station.domain.StationTest.지하철_생성;
import static org.assertj.core.api.Assertions.*;

class PathFinderTest {

    private Station 교대역, 강남역, 역삼역, 선릉역, 한티역, 남부터미널역, 양재역, 매봉역, 도곡역, 양재시민의숲역, 청계산입구역, 판교역, 정자역, 미금역, 동천역, 이매역, 오이도역, 정왕역, 신길온천역, 안산역, 초지역, 고잔역;
    private Line 이호선, 삼호선, 사호선, 신분당선, 수인분당선, 경강선;
    private final int ADDITIONAL_LINE_FARE = 300;

    /**
     *   교대역--(2호선, 3)--강남역--(2호선, 3)--역삼역--(2호선, 3)--선릉역
     *     |                   |                               (수인분당, 3)
     *  (3호선, 5)          (신분당, 3)                             한티역
     *     |                    |                               (수인분당, 3)
     *  남부터미널역--(3호선, 3)--양재역--(3호선, 3)--매봉역--(3호선, 3)--도곡역
     *                     (신분당, 10)
     *                    양재시민의숲역--(신분당선, 1)--청계산입구역--(신분당선, 39)--판교역--(신분당선, 1)--정자역--(신분당선, 7)--미금역--(신분당선, 1)--동천역
     *                                                    (경강선, 1)
     *                                                      이매역
     *
     * 오이도역--(4호선, 10)--정왕역--(4호선, 1)--신길온천역--(4호선, 39)--안산역--(4호선, 1)--초지역--(4호선, 8)--고잔역
     */

    @BeforeEach
    void setUp() {
        교대역 = 지하철_생성("교대역");
        강남역 = 지하철_생성("강남역");
        역삼역 = 지하철_생성("역삼역");
        선릉역 = 지하철_생성("선릉역");
        한티역 = 지하철_생성("한티역");
        남부터미널역 = 지하철_생성("남부터미널역");
        양재역 = 지하철_생성("양재역");
        매봉역 = 지하철_생성("매봉역");
        도곡역 = 지하철_생성("도곡역");
        양재시민의숲역 = 지하철_생성("양재시민의숲역");
        청계산입구역 = 지하철_생성("청계산입구역");
        판교역 = 지하철_생성("판교역");
        정자역 = 지하철_생성("정자역");
        미금역 = 지하철_생성("미금역");
        동천역 = 지하철_생성("동천역");
        이매역 = 지하철_생성("이매역");
        오이도역 = 지하철_생성("오이도역");
        정왕역 = 지하철_생성("정왕역");
        신길온천역 = 지하철_생성("신길온천역");
        안산역 = 지하철_생성("안산역");
        초지역 = 지하철_생성("초지역");
        고잔역 = 지하철_생성("고잔역");

        이호선 = 노선_생성("이호선", "bg-green-color", 교대역, 강남역, 3);
        이호선.addSection(강남역, 역삼역, 3);
        이호선.addSection(역삼역, 선릉역, 3);

        삼호선 = 노선_생성("삼호선", "bg-orange-color", 교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);
        삼호선.addSection(양재역, 매봉역, 3);
        삼호선.addSection(매봉역, 도곡역, 3);

        신분당선 = 노선_생성("신분당선", "bg-red-color", 강남역, 양재역, 3);
        신분당선.addSection(양재역, 양재시민의숲역, 10);
        신분당선.addSection(양재시민의숲역, 청계산입구역, 1);
        신분당선.addSection(청계산입구역, 판교역, 39);
        신분당선.addSection(판교역, 정자역, 1);
        신분당선.addSection(정자역, 미금역, 7);
        신분당선.addSection(미금역, 동천역, 1);

        수인분당선 = 노선_생성("수인분당선", "bg-yellow-color", 선릉역, 한티역, 3);
        수인분당선.addSection(한티역, 도곡역, 3);

        경강선 = 노선_생성("경강선", "bg-blue-color", 판교역, 이매역, 1, ADDITIONAL_LINE_FARE);

        사호선 = 노선_생성("사호선", "bg-sky-color", 오이도역, 정왕역, 10, ADDITIONAL_LINE_FARE);
        사호선.addSection(정왕역, 신길온천역, 1);
        사호선.addSection(신길온천역, 안산역, 39);
        사호선.addSection(안산역, 초지역, 1);
        사호선.addSection(초지역, 고잔역, 8);
    }

    @DisplayName("노선 목록 중 하나의 노선에 속한 2개의 역의 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneLine() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 선릉역, AgeGroup.ADULT);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 역삼역, 선릉역);
        최소_노선_길이_일치됨(stations.getDistance(), 9);
    }

    @DisplayName("노선 목록 중 한번의 환승을 통해 도달할 수 있는 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneTransfer() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선, 신분당선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 양재역, AgeGroup.ADULT);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역);
        최소_노선_길이_일치됨(stations.getDistance(), 6);
    }

    @DisplayName("노선 목록 중 도달할 수 있는 여러 경로를 가진 목적지의 경로를 조회하면 최소 경로로 조회해야 한다")
    @Test
    void findShortestPathByMultiplePath() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선, 삼호선, 신분당선, 수인분당선));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(교대역, 도곡역, AgeGroup.ADULT);

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역, 매봉역, 도곡역);
        최소_노선_길이_일치됨(stations.getDistance(), 12);
    }

    @DisplayName("출발역과 도착역이 같은 노선을 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathBySameStartAndEndStation() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(이호선));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역, AgeGroup.ADULT));
    }

    @DisplayName("도달할 수 없는 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathByUnreachablePath() {
        // given
        PathFinder pathFinder = new PathFinder(라인_목록_생성(신분당선, 수인분당선));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(강남역, 도곡역, AgeGroup.ADULT));
        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findShortestPath(한티역, 강남역, AgeGroup.ADULT));
    }

    @DisplayName("기본 운임 비용 노선의 경로 조회 시 거리에 따른 요금이 정상 계산되어야 한다")
    @Test
    void findShortestPathFareByDefaultFareLine() {
        // given
        PathFinder defaultFareFinder = new PathFinder((라인_목록_생성(신분당선)));

        // when
        ShortestPathResponse defaultFareFinderPath = defaultFareFinder.findShortestPath(양재역, 양재시민의숲역, AgeGroup.ADULT);
        ShortestPathResponse defaultAdditionalFarePathByMinDistance = defaultFareFinder.findShortestPath(양재역, 청계산입구역, AgeGroup.ADULT);
        ShortestPathResponse defaultAdditionalFarePathByMaxDistance = defaultFareFinder.findShortestPath(양재역, 판교역, AgeGroup.ADULT);
        ShortestPathResponse defaultLongerFarePathByMinDistance = defaultFareFinder.findShortestPath(양재역, 정자역, AgeGroup.ADULT);
        ShortestPathResponse defaultLongerFarePathByMoreDistance = defaultFareFinder.findShortestPath(양재역, 미금역, AgeGroup.ADULT);
        ShortestPathResponse defaultLongerFarePathByMoreDistance2 = defaultFareFinder.findShortestPath(양재역, 동천역, AgeGroup.ADULT);

        // then
        노선_요금_일치됨(defaultFareFinderPath, 1_250);                    // 10 km
        노선_요금_일치됨(defaultAdditionalFarePathByMinDistance, 1_350);   // 11 km
        노선_요금_일치됨(defaultAdditionalFarePathByMaxDistance, 2_050);   // 50 km
        노선_요금_일치됨(defaultLongerFarePathByMinDistance, 2_150);       // 51 km
        노선_요금_일치됨(defaultLongerFarePathByMoreDistance, 2_150);      // 58 km
        노선_요금_일치됨(defaultLongerFarePathByMoreDistance2, 2_250);     // 59 km
    }

    @DisplayName("추가 요금이 존재하는 노선의 경로 조회 시 거리에 따른 요금과 추가 요금이 정상 계산되어야 한다")
    @Test
    void findShortestPathFareByAdditionalFareLine() {
        // given
        PathFinder defaultFareFinder = new PathFinder((라인_목록_생성(사호선)));

        // when
        ShortestPathResponse defaultFareFinderPath = defaultFareFinder.findShortestPath(오이도역, 정왕역, AgeGroup.ADULT);
        ShortestPathResponse defaultAdditionalFarePathByMinDistance = defaultFareFinder.findShortestPath(오이도역, 신길온천역, AgeGroup.ADULT);
        ShortestPathResponse defaultAdditionalFarePathByMaxDistance = defaultFareFinder.findShortestPath(오이도역, 안산역, AgeGroup.ADULT);
        ShortestPathResponse defaultLongerFarePathByMinDistance = defaultFareFinder.findShortestPath(오이도역, 초지역, AgeGroup.ADULT);
        ShortestPathResponse defaultLongerFarePathByMoreDistance = defaultFareFinder.findShortestPath(오이도역, 고잔역, AgeGroup.ADULT);

        // then
        노선_요금_일치됨(defaultFareFinderPath, 1_250 + ADDITIONAL_LINE_FARE);                     // 10 km
        노선_요금_일치됨(defaultAdditionalFarePathByMinDistance, 1_350 + ADDITIONAL_LINE_FARE);    // 11 km
        노선_요금_일치됨(defaultAdditionalFarePathByMaxDistance, 2_050 + ADDITIONAL_LINE_FARE);    // 50 km
        노선_요금_일치됨(defaultLongerFarePathByMinDistance, 2_150 + ADDITIONAL_LINE_FARE);        // 51 km
        노선_요금_일치됨(defaultLongerFarePathByMoreDistance, 2_250 + ADDITIONAL_LINE_FARE);       // 59 km
    }

    @DisplayName("경로 조회 시 여러개의 노선이 존재하면 해당 노선 중 가장 비싼 노선의 운임 비용으로 요금이 정상 계산되어야 한다")
    @Test
    void findShortestMultiplePatFareTest() {
        // given
        PathFinder pathFinder = new PathFinder((라인_목록_생성(신분당선, 경강선)));

        // when
        ShortestPathResponse stations = pathFinder.findShortestPath(판교역, 이매역, AgeGroup.ADULT);

        // then
        노선_요금_일치됨(stations, 1_250 + ADDITIONAL_LINE_FARE);
    }

    @DisplayName("경로 조회 시 연령별 할인율이 적용되어 계산되어야 한다")
    @ParameterizedTest
    @EnumSource(value = AgeGroup.class)
    void findShortestPathWithAgeGroupTest(AgeGroup ageGroup) {
        // given
        PathFinder pathFinder = new PathFinder((라인_목록_생성(신분당선, 경강선)));

        // when
        ShortestPathResponse stationsWithAgeGroup = pathFinder.findShortestPath(판교역, 이매역, ageGroup);

        // then
        노선_요금_일치됨(stationsWithAgeGroup, ageGroup, 1_250, ADDITIONAL_LINE_FARE);
    }

    private void 최소_노선_경로_일치됨(ShortestPathResponse source, Station... target) {
        List<PathStation> stations = source.getStations();

        assertThat(stations.size()).isEqualTo(target.length);

        for (int idx = 0; idx < stations.size(); idx++) {
            assertThat(stations.get(idx).getName()).isEqualTo(target[idx].getName());
        }
    }

    private void 최소_노선_길이_일치됨(int sourceDistance, int targetDistance) {
        assertThat(sourceDistance).isEqualTo(targetDistance);
    }

    private void 노선_요금_일치됨(ShortestPathResponse source, int expectedFare) {
        assertThat(source.getTotalFare()).isEqualTo(expectedFare);
    }

    private void 노선_요금_일치됨(ShortestPathResponse source, AgeGroup ageGroup, int originalLineFare, int additionalLineFare) {
        Integer discountResult = null;
        if (ageGroup == AgeGroup.ADULT) {
            discountResult = originalLineFare + additionalLineFare;
        }
        if (ageGroup == AgeGroup.TEENAGER) {
            discountResult = (int) ((originalLineFare + additionalLineFare - 350) * 0.8);
        }
        if (ageGroup == AgeGroup.CHILD) {
            discountResult = (int) ((originalLineFare + additionalLineFare - 350) * 0.5);
        }

        if (discountResult == null || discountResult == 0) {
            fail("할인 결과를 계산할 수 없습니다.");
        }
        assertThat(source.getTotalFare()).isEqualTo(discountResult);
    }
}
