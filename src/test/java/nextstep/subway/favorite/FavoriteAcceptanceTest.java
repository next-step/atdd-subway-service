package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSteps.로그인_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceSteps.*;
import static nextstep.subway.line.acceptance.LineAcceptanceSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceSteps.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "email@email.com";
    private final String PASSWORD = "password";
    private final Integer AGE = 20;
    private String 사용자;

    private Long 신분당선;
    private Long 강남역;
    private Long 양재역;
    private Long 정자역;
    private Long 광교역;

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class).getId();
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class).getId();
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class).getId();
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class).getId();
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역, 광교역, 10)).as(LineResponse.class).getId();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    /**
     * Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @Test
    @DisplayName("즐겨찾기를 관리한다.")
    void manageMember() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성을_요청(사용자, 강남역, 정자역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_목록을_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회_요청됨(즐겨찾기_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제를_요청(사용자, 강남역);
        // then
        즐겨찾기_삭제_요청됨(즐겨찾기_삭제_응답);
    }
}
