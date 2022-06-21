package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.fare.domain.DistanceExtraFare;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final int CHILD_AGE = 6;
    public static final int STUDENT_AGE = 13;
    public static final int ADULT_AGE = 19;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private String 어린이;
    private String 청소년;
    private String 성인;

    /**
     * 교대역   --- *2호선* 10km ---   강남역
     * |                             |
     * *3호선* 3km                *신분당선* 10km
     * |                             |
     * 남부터미널역 --- *3호선* 2km ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 200);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 300);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, CHILD_AGE);
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD, STUDENT_AGE);
        MemberAcceptanceTest.회원_생성을_요청("a@b.c", MemberAcceptanceTest.NEW_PASSWORD, ADULT_AGE);
        어린이 = AuthAcceptanceTest.로그인되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        청소년 = AuthAcceptanceTest.로그인되어_있음(MemberAcceptanceTest.NEW_EMAIL, MemberAcceptanceTest.NEW_PASSWORD);
        성인 = AuthAcceptanceTest.로그인되어_있음("a@b.c", MemberAcceptanceTest.NEW_PASSWORD);
    }

    /**
     * Feature: 비로그인 시 지하철 경로 검색
     * <p>
     * Scenario: 로그인하지 않았을 때 두 역의 최단 거리 경로 조회
     * Given 지하철역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리도 함께 응답
     * And 지하철 이용 요금도 함께 응답
     */
    @DisplayName("최단 경로를 조회한다.")
    @Test
    void 비로그인_회원_경로_거리_요금_조회() {
        int 예상_총_거리 = 12;
        int 기본_요금 = DistanceExtraFare.BASE_FARE;
        int 거리_10km_초과_요금 = 100;
        int 신분당선_추가_요금 = 신분당선.getExtraFare();
        int 일반_요금 = 기본_요금 + 거리_10km_초과_요금 + 신분당선_추가_요금;
        // when
        ExtractableResponse<Response> response = 지하철_최단_경로_조회_요청(강남역, 남부터미널역);

        // then
        PathResponse path = response.as(PathResponse.class);
        assertAll(
                () -> assertThat(path.getStations()).containsExactly(강남역, 양재역, 남부터미널역),
                () -> assertThat(path.getDistance()).isEqualTo(예상_총_거리),
                () -> assertThat(path.getFare()).isEqualTo(일반_요금)
        );
    }

    /**
     * Feature: 로그인 시 지하철 경로 검색
     * <p>
     * Scenario: 로그인했을 때 두 역의 최단 거리 경로 조회
     * Given 지하철역이 등록되어 있음
     * And 지하철 노선이 등록되어 있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * And 회원(어린이/청소년/성인) 로그인되어 있음
     * When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
     * Then 최단 거리 경로를 응답
     * And 총 거리도 함께 응답
     * And 지하철 이용 요금도 함께 응답
     */
    @DisplayName("로그인한 회원이 최단 경로와 총 거리와 요금을 조회한다.")
    @Test
    void 로그인_회원_경로_거리_요금_조회() {
        int 예상_총_거리 = 12;
        int 기본_요금 = DistanceExtraFare.BASE_FARE;
        int 거리_10km_초과_요금 = 100;
        int 신분당선_추가_요금 = 신분당선.getExtraFare();
        int 일반_요금 = 기본_요금 + 거리_10km_초과_요금 + 신분당선_추가_요금;
        // when
        ExtractableResponse<Response> adultResponse = 로그인_지하철_최단_경로_조회_요청(성인, 강남역, 남부터미널역);
        // then
        PathResponse adultPath = adultResponse.as(PathResponse.class);
        assertAll(
                () -> assertThat(adultPath.getStations()).containsExactly(강남역, 양재역, 남부터미널역),
                () -> assertThat(adultPath.getDistance()).isEqualTo(예상_총_거리),
                () -> assertThat(adultPath.getFare()).isEqualTo(일반_요금)
        );

        int 어린이_할인_시_공제액 = 350;
        double 어린이_할인율 = 0.5;
        int 어린이_할인_금액 = (int) ((일반_요금 - 어린이_할인_시_공제액) * 어린이_할인율);
        int 어린이_할인_요금 = 일반_요금 - 어린이_할인_금액;
        // when
        ExtractableResponse<Response> childResponse = 로그인_지하철_최단_경로_조회_요청(어린이, 강남역, 남부터미널역);
        // then
        PathResponse childPath = childResponse.as(PathResponse.class);
        assertAll(
                () -> assertThat(childPath.getStations()).containsExactly(강남역, 양재역, 남부터미널역),
                () -> assertThat(childPath.getDistance()).isEqualTo(예상_총_거리),
                () -> assertThat(childPath.getFare()).isEqualTo(어린이_할인_요금)
        );

        int 청소년_할인_시_공제액 = 350;
        double 청소년_할인율 = 0.2;
        int 청소년_할인_금액 = (int) ((일반_요금 - 청소년_할인_시_공제액) * 청소년_할인율);
        int 청소년_할인_요금 = 일반_요금 - 청소년_할인_금액;
        // when
        ExtractableResponse<Response> studentResponse = 로그인_지하철_최단_경로_조회_요청(청소년, 강남역, 남부터미널역);
        // then
        PathResponse studentPath = studentResponse.as(PathResponse.class);
        assertAll(
                () -> assertThat(studentPath.getStations()).containsExactly(강남역, 양재역, 남부터미널역),
                () -> assertThat(studentPath.getDistance()).isEqualTo(예상_총_거리),
                () -> assertThat(studentPath.getFare()).isEqualTo(청소년_할인_요금)
        );
    }

    private LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFare) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFare)).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_최단_경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_지하철_최단_경로_조회_요청(String 사용자, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source.getId(), target.getId())
                .then().log().all()
                .extract();
    }
}
