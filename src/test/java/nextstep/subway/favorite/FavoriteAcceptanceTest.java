package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceRequest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceResponse.로그인_성공_토큰_반환;
import static nextstep.subway.favorite.FavoriteAcceptanceRequest.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceRequest.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceRequest.즐겨찾기_조회_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceResponse.즐겨찾기_삭제_요청_성공;
import static nextstep.subway.favorite.FavoriteAcceptanceResponse.즐겨찾기_생성_요청_성공;
import static nextstep.subway.favorite.FavoriteAcceptanceResponse.즐겨찾기_생성_요청_실패;
import static nextstep.subway.favorite.FavoriteAcceptanceResponse.즐겨찾기_조회_요청_성공;
import static nextstep.subway.member.MemberAcceptanceRequest.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceResponse.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/*
Feature: 즐겨찾기를 관리한다.
  Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음
  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private final String INVALID_TOKEN = "invalidToken";
    private String ACCESS_TOKEN;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 삼성역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> registerResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(registerResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        ACCESS_TOKEN = 로그인_성공_토큰_반환(loginResponse);

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
    }

    @DisplayName("토큰이 유효하면 즐겨찾기를 생성한다")
    @Test
    void 즐겨찾기_생성_성공() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(ACCESS_TOKEN, 강남역.getId(), 역삼역.getId());

        즐겨찾기_생성_요청_성공(response);
    }

    @DisplayName("토큰이 유효하면 지하철역을 즐겨찾기로 등록, 조회, 삭제한다")
    @TestFactory
    Stream<DynamicTest> register_get_delete_Favorite() {
        return Stream.of(
                dynamicTest("즐겨찾기를 생성한다", () -> {
                    ExtractableResponse<Response> register_response_1 = 즐겨찾기_생성_요청(ACCESS_TOKEN, 강남역.getId(), 역삼역.getId());
                    즐겨찾기_생성_요청_성공(register_response_1);

                    ExtractableResponse<Response> register_response_2 = 즐겨찾기_생성_요청(ACCESS_TOKEN, 강남역.getId(), 삼성역.getId());
                    즐겨찾기_생성_요청_성공(register_response_2);
                }),
                dynamicTest("생성된 즐겨찾기를 조회한다", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_조회_요청(ACCESS_TOKEN);
                    즐겨찾기_조회_요청_성공(response);
                }),
                dynamicTest("생성된 즐겨찾기를 삭제한다", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(ACCESS_TOKEN, 강남역.getId());
                    즐겨찾기_삭제_요청_성공(response);
                })
        );
    }

    @DisplayName("토큰이 유효하지 않다면 실패한다.")
    @Test
    void 즐겨찾기_생성_토큰_실패() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(INVALID_TOKEN, 강남역.getId(), 역삼역.getId());

        즐겨찾기_생성_요청_실패(response);
    }
}
