package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.TestToken;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.AuthSteps.로그인_요청;
import static nextstep.subway.favorite.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSetionSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemeberSteps.회원_등록_되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 정자역;
    private LineResponse 신분당선;
    private static final String EMAIL = "bbbnam@naver.com";
    private static final String PASSWORD = "12345";
    private static final int AGE = 34;
    private TestToken 사용자_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 정자역, 5);
        회원_등록_되어_있음(EMAIL, PASSWORD, AGE);
        사용자_토큰 = 로그인_요청(EMAIL, PASSWORD).as(TestToken.class);
    }

    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageFavorites() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청(사용자_토큰, 강남역, 정자역);
        즐겨찾기_생성됨(createdResponse);

        ExtractableResponse<Response> findedResponse = 즐겨찾기_목록_조회_요청(사용자_토큰);
        즐겨찾기_목록_조회됨(findedResponse);
    }
}