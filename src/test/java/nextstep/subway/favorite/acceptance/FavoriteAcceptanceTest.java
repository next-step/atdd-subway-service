package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);
    }

    /**
     * Feature: 즐겨찾기 생성
     *   Scenario: 새로운 회원을 등록하고 즐겨찾기를 추가한다.
     *     Given 지하철역이 생성되어 있음
     *     When 회원 생성 요청
     *     Then 새로운 회원이 생성됨
     *     When 로그인 실패
     *     Then 즐겨찾기 추가 실패
     *     When 로그인 성공 후에 경로 즐겨찾기 추가
     *     Then 즐겨찾기 추가됨
     */
    @DisplayName("즐겨찾기 관련 기능")
    @TestFactory
    Stream<DynamicTest> favorite() {
        return Stream.of(
                dynamicTest("새로운 회원을 등록한다", 회원_생성_요청_성공(EMAIL, PASSWORD, AGE)),
                dynamicTest("로그인에 실패하면 즐겨찾기 추가를 할 수 없다", 즐겨찾기_추가_요청_실패(EMAIL, WRONG_PASSWORD)),
                dynamicTest("등록한 회원 로그인 후에 즐겨찾기를 추가한다", 즐겨찾기_추가_요청_성공(EMAIL, PASSWORD))
        );
    }

    public Executable 즐겨찾기_추가_요청_실패(String email, String password) {
        return () -> {
            ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(email, password));
            로그인_실패한다(tokenResponse);

            FavoriteRequest request = FavoriteRequest.of(강남역.getId(), 양재역.getId());
            ExtractableResponse<Response> response = 즐겨찾기_추가_요청("", request);
            즐겨찾기_추가_실패(response);
        };
    }

    public Executable 즐겨찾기_추가_요청_성공(String email, String password) {
        return () -> {
            ExtractableResponse<Response> tokenResponse = 로그인_요청(new TokenRequest(email, password));
            로그인_성공하고_토큰을_발급받는다(tokenResponse);

            String accessToken = tokenResponse.jsonPath().getString("accessToken");
            FavoriteRequest request = FavoriteRequest.of(강남역.getId(), 양재역.getId());

            ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, request);
            즐겨찾기_추가_성공(response);
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

    public static void 즐겨찾기_추가_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertAll(() -> {
            assertThat(favoriteResponse.getId()).isNotNull();
            assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역");
            assertThat(favoriteResponse.getTarget().getName()).isEqualTo("양재역");
        });
    }

    public static void 즐겨찾기_추가_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
