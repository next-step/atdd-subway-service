package nextstep.subway.favorite.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;
    private static ExtractableResponse<Response> 즐겨찾기_전체조회_응답;
    private static List<FavoriteResponse> 즐겨찾기_전체목록;
    private static String 사용자_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 1000);
        LineAcceptanceTest.지하철_노선_생성_요청(신분당선);
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자_토큰 = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD)
                .as(TokenResponse.class)
                .getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @TestFactory
    Stream<DynamicTest> manageFavorite() {
        return Stream.of(
                dynamicTest("강남역-양재역 즐겨찾기 생성을 요청하면, 즐겨찾기가 생성된다.", () -> {
                    //when
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자_토큰, 강남역.getId(), 광교역.getId());

                    //then
                    응답결과_확인(createResponse, HttpStatus.CREATED);
                }),

                dynamicTest("사용자의 전체 즐겨찾기 조회를 요청하면, 강남역-양재역 즐겨찾기가 조회된다.", () -> {
                    //when
                    즐겨찾기_전체조회_응답 = 즐겨찾기_목록_조회_요청(사용자_토큰);
                    즐겨찾기_전체목록 = Arrays.asList(즐겨찾기_전체조회_응답.body().as(FavoriteResponse[].class));

                    //then
                    응답결과_확인(즐겨찾기_전체조회_응답, HttpStatus.OK);
                    즐겨찾기_목록_조회됨(즐겨찾기_전체목록);
                }),

                dynamicTest("강남역-양재역 즐겨찾기 삭제를 요청하면, 강남역-양재역 즐겨찾기가 삭제된다.", () -> {
                    //when
                    ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자_토큰, 즐겨찾기_전체목록.get(0).getId());

                    //then
                    응답결과_확인(deleteResponse, HttpStatus.NO_CONTENT);
                })
        );
    }

    /**
     * Feature: 즐겨찾기를 관리에 실패한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 출발지와 도착지를 같은 역으로 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성에 실패
     *     When 존재하지 않는 지하철역으로 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성에 실패
     *     When 존재하지 않는 회원으로 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성에 실패
     */
    @TestFactory
    Stream<DynamicTest> invalid_manageFavorite() {
        return Stream.of(
                dynamicTest("강남역-강남역 즐겨찾기 생성을 요청하면, INTERNAL_SERVER_ERROR 가 발생한다.", () -> {
                    //when
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자_토큰, 강남역.getId(), 강남역.getId());

                    //then
                    응답결과_확인(createResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }),

                dynamicTest("존재하지 않는 지하철역으로 즐겨찾기 생성을 요청하면, INTERNAL_SERVER_ERROR 가 발생한다.", () -> {
                    //given
                    Long 존재하지_않는_지하철역_아이디 = 99L;

                    //when
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자_토큰, 존재하지_않는_지하철역_아이디, 강남역.getId());

                    //then
                    응답결과_확인(createResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }),


                dynamicTest("존재하지 않는 회원으로 즐겨찾기 생성을 요청하면, INTERNAL_SERVER_ERROR 가 발생한다.", () -> {
                    //given
                    String 존재하지_않는_회원_토큰 = "토큰";

                    //when
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(존재하지_않는_회원_토큰, 강남역.getId(), 광교역.getId());

                    //then
                    응답결과_확인(createResponse, HttpStatus.UNAUTHORIZED);
                })
        );
    }

    private void 즐겨찾기_목록_조회됨(List<FavoriteResponse> favoriteResponses) {
        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(광교역.getId());
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .body(new FavoriteRequest(sourceStationId, targetStationId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .when().delete("/favorites/" + favoriteId)
                .then().log().all()
                .extract();
    }
}
