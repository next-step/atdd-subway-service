package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceFactory.로그인_요청시도;
import static nextstep.subway.favorite.FavoriteAcceptanceFactory.즐겨찾기_생성_시도;
import static nextstep.subway.favorite.FavoriteAcceptanceFactory.즐겨찾기_생성완료;
import static nextstep.subway.member.MemberAcceptanceFactory.회원_생성을_요청;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String YANG_EMAIL = "rhfpdk92@naver.com";
    private static final String YANG_PASSWORD = "password";
    private static final Integer YANG_AGE = 31;

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private TokenResponse 유효한_토큰;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        회원_생성을_요청(YANG_EMAIL, YANG_PASSWORD, YANG_AGE);
        유효한_토큰 = 로그인_요청시도(new TokenRequest(YANG_EMAIL, YANG_PASSWORD)).as(TokenResponse.class);
    }

    /**s
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     */
    @Test
    void 즐겨찾기를_생성한다() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_시도(유효한_토큰, 강남역, 정자역);

        즐겨찾기_생성완료(createResponse);
    }
}
