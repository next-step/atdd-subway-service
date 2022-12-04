package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private String token;

    /**
     * Given 지하철역 / 노선 / 구간이 등록되어 있다
     * And 회원이 등록되어 있다
     * And 로그인 되어 있다
     */
    @BeforeEach
    void setup() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD);
        token = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    /**
     * When 즐겨찾기 생성 요청을 하면
     * Then 즐겨찾기가 생성된다
     */
    @Test
    @DisplayName("즐겨찾기 생성을 요청한다")
    void 즐겨찾기_생성() {
        // when

        // then
    }

    /**
     * When 즐겨찾기 목록 조회 요청을 하면
     * Then 즐겨찾기 목록이 조회된다
     */
    @Test
    @DisplayName("즐겨찾기 목록을 조회한다")
    void 즐겨찾기_목록_조회() {
        // when

        // then
    }

    /**
     * When 즐겨찾기 삭제 요청을 하면
     * Then 즐겨찾기가 삭제된다
     */
    @Test
    @DisplayName("즐겨찾기 삭제를 요청한다")
    void 즐겨찾기_삭제_요청() {
        // when

        // then
    }
}
