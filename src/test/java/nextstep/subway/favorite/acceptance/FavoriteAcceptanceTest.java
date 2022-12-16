package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        accessToken = tokenResponse.jsonPath().getString("accessToken");
    }

    /**
     * Feature: 즐겨찾기 관리
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 등록된 회원으로 토큰이 발급되어 있음
     *   Scenario: 즐겨찾기를 추가, 삭제, 조회한다.
     *     When 로그인 실패
     *     Then 즐겨찾기 추가 실패
     *     When 즐겨찾기 추가 요청
     *     Then 즐겨찾기 추가됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 같은 경로로 즐겨찾기 추가 요청
     *     Then 중복으로 즐겨찾기 추가 실패
     *     When 다른 사용자의 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제 실패
     *     When 나의 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 관련 기능")
    @TestFactory
    Stream<DynamicTest> favorite() {
        return Stream.of(
                dynamicTest("로그인에 실패하면 즐겨찾기 추가를 할 수 없다", 토큰이_없으면_즐겨찾기_추가_요청_실패()),
                dynamicTest("등록한 회원 로그인 후에 즐겨찾기를 추가한다", 즐겨찾기_추가_요청_성공()),
                dynamicTest("로그인 성공 회원은 즐겨찾기 목록을 조회 할 수 있다", 즐겨찾기_목록_조회_요청_성공()),
                dynamicTest("같은 경로는 즐겨찾기 할 수 없다", 중복이면_즐겨찾기_추가_요청_실패()),
                dynamicTest("다른 사용자의 즐겨찾기는 삭제 할 수 없다", 다른_사용자의_즐겨찾기_삭제_요청_실패()),
                dynamicTest("즐겨찾기 삭제에 성공한다", 즐겨찾기_삭제_요청_성공())
        );
    }

    public Executable 토큰이_없으면_즐겨찾기_추가_요청_실패() {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_추가_요청("", FavoriteRequest.of(강남역.getId(), 양재역.getId()));
            인증에_실패하여_즐겨찾기_추가_실패(response);
        };
    }

    public Executable 즐겨찾기_추가_요청_성공() {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, FavoriteRequest.of(강남역.getId(), 양재역.getId()));
            즐겨찾기_추가_성공(response);
        };
    }

    public Executable 즐겨찾기_목록_조회_요청_성공() {
        return () -> {
            ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
            즐겨찾기_조회_성공(response);
        };
    }

    public Executable 중복이면_즐겨찾기_추가_요청_실패() {
        return () -> {
            ExtractableResponse<Response> duplicateResponse = 즐겨찾기_추가_요청(accessToken, FavoriteRequest.of(강남역.getId(), 양재역.getId()));
            중복으로_즐겨찾기_추가_실패(duplicateResponse);
        };
    }

    public Executable 다른_사용자의_즐겨찾기_삭제_요청_실패() {
        return () -> {
            ExtractableResponse<Response> createResponse = 회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
            회원_생성됨(createResponse);

            ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(NEW_EMAIL, NEW_PASSWORD));
            String newAccessToken = tokenResponse.jsonPath().getString("accessToken");

            ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(newAccessToken, 1L);
            다른_사용자의_즐겨찾기_삭제_실패(deleteResponse);
        };
    }

    public Executable 즐겨찾기_삭제_요청_성공() {
        return () -> {
            ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 1L);
            즐겨찾기_삭제_성공(deleteResponse);

            ExtractableResponse<Response> favoriteResponse = 즐겨찾기_조회_요청(accessToken);
            즐겨찾기_삭제되어_조회되는_즐겨찾기가_없음(favoriteResponse);
        };
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, FavoriteRequest params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().
                extract();
    }

    public static void 즐겨찾기_추가_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 인증에_실패하여_즐겨찾기_추가_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<StationResponse> stationResponses = response.jsonPath().get();
        assertThat(stationResponses).hasSize(1);
    }

    public static void 중복으로_즐겨찾기_추가_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 다른_사용자의_즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제되어_조회되는_즐겨찾기가_없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<StationResponse> stationResponses = response.jsonPath().get();
        assertThat(stationResponses).hasSize(0);
    }
}
