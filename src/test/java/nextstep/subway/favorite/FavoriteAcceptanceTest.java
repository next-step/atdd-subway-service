package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인을_성공하면_토큰을_발급받는다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

/**
 * Feature: 즐겨찾기를 관리한다.
 * <p>
 * Background
 * Given 지하철역 등록되어 있음
 * And 지하철 노선 등록되어 있음
 * And 지하철 노선에 지하철역 등록되어 있음
 * And 회원 등록되어 있음
 * And 로그인 되어있음
 * <p>
 * Scenario: 즐겨찾기를 관리
 * When 즐겨찾기 생성을 요청
 * Then 즐겨찾기 생성됨
 * When 즐겨찾기 목록 조회 요청
 * Then 즐겨찾기 목록 조회됨
 * When 즐겨찾기 삭제 요청
 * Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private String 토큰;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        일호선_등록되어_있음();
        토큰 = 로그인을_성공하면_토큰을_발급받는다(로그인_요청함(회원_등록되어_있음(EMAIL, PASSWORD, AGE)));
    }

    @DisplayName("")
    @Test
    void test() {
        // given

        // when
        RestAssured
                .given().log().all()
                .when()
                .then().log().all();

        // then
    }

    private void 일호선_등록되어_있음() {
        StationResponse 인천 = 지하철역_등록되어_있음("인천").as(StationResponse.class);
        StationResponse 소요산 = 지하철역_등록되어_있음("소요산").as(StationResponse.class);

        ExtractableResponse<Response> response = 지하철_노선_등록되어_있음(new LineRequest("일호선", "남색", 인천.getId(), 소요산.getId(), 100));

        LineResponse lineResponse = response.jsonPath().getObject("", LineResponse.class);
    }


}