package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.정상_로그인_토큰_반환;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
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
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final  int AGE = 20;
    private String 토큰;

    /**
     * 교대역    --- *2호선* ---  강남역
     * |                        |
     * *3호선*                 *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(LineRequest.from("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        회원_생성됨(회원_생성을_요청(EMAIL, PASSWORD, AGE));
        토큰 = 정상_로그인_토큰_반환(EMAIL, PASSWORD);
    }

    /**
     * Feature: 즐겨찾기 관련 기능
     *
     * Background
     * * Given 지하철역 등록되어 있음
     * * And 지하철 노선 등록되어 있음
     * * And 지하철 노선에 지하철역 등록되어 있음
     * * And 회원 등록되어 있음
     * * And 로그인 되어있음
     *
     * Scenario: 즐겨찾기를 관리
     * * When 즐겨찾기 생성을 요청
     * * Then 즐겨찾기 생성됨
     * * When 즐겨찾기 목록 조회 요청
     * * Then 즐겨찾기 목록 조회됨
     * * When 즐겨찾기 삭제 요청
     * * Then 즐겨찾기 삭제됨
     */
    @DisplayName("나의 회원정보 관리 통합 테스트")
    @TestFactory
    Stream<DynamicTest> dynamicTestsFromCollection() {
        Long 강남역_ID = 강남역.getId();
        Long 남부터미널역_ID = 남부터미널역.getId();
        FavoriteRequest 즐겨찾기_요청 = FavoriteRequest.of(강남역_ID, 남부터미널역_ID);
        return Stream.of(
                dynamicTest("나의 즐겨찾기를 생성 할 수 있다.", () -> 나의_즐겨찾기를_추가하고_검증한다(토큰,즐겨찾기_요청)),
                dynamicTest("나의 즐겨찾기를 조회 할 수 있다.", () -> 나의_즐겨찾기를_조회하고_검증한다(토큰, 강남역_ID, 남부터미널역_ID)),
                dynamicTest("나의 즐겨찾기를 삭제 할 수 있다.", () -> 나의_즐겨찾기를_삭제하고_검증한다(토큰))
        );}

    void 나의_즐겨찾기를_추가하고_검증한다(String 토큰, FavoriteRequest 즐겨찾기_요청) {
        // when
        ExtractableResponse<Response> 생성_결과 = 나의_즐겨찾기_추가_요청(토큰, 즐겨찾기_요청);
        // then
        즐겨찾기_생성됨(생성_결과);
    }

    void 나의_즐겨찾기를_조회하고_검증한다(String 토큰, Long source, Long target) {
        // when
        ExtractableResponse<Response> 조회_결과 = 나의_즐겨찾기_전체_조회_요청(토큰);
        // then
        즐겨찾기_조회됨(조회_결과, source, target);
    }

    void 나의_즐겨찾기를_삭제하고_검증한다(String 토큰) {
        Long id = 첫번째_즐겨찾기_아이디_찾기();
        // when
        ExtractableResponse<Response> 삭제_결과 = 나의_즐겨찾기_삭제_요청(토큰, id);
        // then
        즐겨찾기_삭제됨(삭제_결과, id);
    }

    private Long 첫번째_즐겨찾기_아이디_찾기(){
        ExtractableResponse<Response> 조회_결과 = 나의_즐겨찾기_전체_조회_요청(토큰);
        return 조회_결과.jsonPath().getList("id", Long.class).get(0);
    }

    public static ExtractableResponse<Response> 나의_즐겨찾기_추가_요청(String token, FavoriteRequest request) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 나의_즐겨찾기_전체_조회_요청(String token) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 나의_즐겨찾기_삭제_요청(String token, Long id) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    public void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(FavoriteResponse.class)).isNotNull();
    }
    public void 즐겨찾기_조회됨(ExtractableResponse<Response> response, Long source, Long target){
        boolean 조회_결과_존재여부 = response.jsonPath()
                .getList(".", FavoriteResponse.class)
                .stream()
                .anyMatch(it->즐겨찾기_일치(it,source,target));
        assertTrue(조회_결과_존재여부);
    }
    public void 즐겨찾기_삭제됨(ExtractableResponse<Response> response, Long id) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertFalse(아이디가_즐겨찾기에_존재함(response, id));
    }
    private boolean 아이디가_즐겨찾기에_존재함(ExtractableResponse<Response> response, Long id){
        return response.jsonPath().getList("id", Long.class).contains(id);
    }
    private boolean 즐겨찾기_일치(FavoriteResponse favoriteResponse, Long source, Long target){
        return favoriteResponse.getSource().getId().equals(source)
                && favoriteResponse.getTarget().getId().equals(target);
    }

}
