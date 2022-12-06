package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

/**
 * Feature: 즐겨찾기를 관리한다.
 *
 * Background
 * Given 지하철역 등록되어 있음
 * And 지하철 노선 등록되어 있음
 * And 지하철 노선에 지하철역 등록되어 있음
 * And 회원 등록되어 있음
 * And 로그인 되어있음
 *
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
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);
        지하철_노선에_지하철역_등록됨(response);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_됨(loginResponse);

        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

}
