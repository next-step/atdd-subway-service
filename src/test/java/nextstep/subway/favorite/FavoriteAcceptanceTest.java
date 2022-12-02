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
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceFixture.로그인_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceFixture.*;
import static nextstep.subway.line.acceptance.LineAcceptanceFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceFixture.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceFixture.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private String 로그인_성공_토큰_값;

    private Long 신림역;
    private Long 강남역;
    private Long 잠실역;
    private Long 왕십리역;
    private Long 이호선;


    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    public void set_up() {
        super.setUp();

        신림역 = 지하철역_등록되어_있음("신림역").as(StationResponse.class).getId();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class).getId();
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class).getId();
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class).getId();

        LineRequest lineRequest = new LineRequest("이호선", "bg-green-600", 신림역, 왕십리역, 10);
        이호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class).getId();

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공_토큰_값 = 로그인_요청_응답.as(TokenResponse.class).getAccessToken();

    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * <p>
     * Given 즐겨찾기 하나 더 생성
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * <p>
     * Given 즐겨찾기 첫번째 목록 식별 번호 조회
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manage_favorite() {

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_성공_토큰_값, 신림역, 강남역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);

        // given
        즐겨찾기_생성_요청(로그인_성공_토큰_값, 강남역, 잠실역);
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(로그인_성공_토큰_값);
        // then
        즐겨찾기_목록_조회_요청됨(즐겨찾기_목록_조회_응답);

        // given
        Long 즐겨찾기_목록_첫번째_응답 = 즐겨찾기_목록_첫번째_요청(즐겨찾기_목록_조회_응답);
        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(로그인_성공_토큰_값, 즐겨찾기_목록_첫번째_응답);
        // then
        즐겨찾기_삭제_요청됨(즐겨찾기_삭제_응답);

    }

    private static Long 즐겨찾기_목록_첫번째_요청(ExtractableResponse<Response> 즐겨찾기_목록_조회_응답) {
        return 즐겨찾기_목록_조회_응답.jsonPath().getList("id", Long.class).get(0);
    }

}
