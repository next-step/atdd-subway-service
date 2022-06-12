package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.getAccessToken;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = getAccessToken(로그인_요청(EMAIL, PASSWORD));
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // When
        ExtractableResponse<Response> 즐겨찾기_생성_결과 = 즐겨찾기_생성을_요청(token, 강남역, 광교역);
        // Then
        즐겨찾기_생성됨(즐겨찾기_생성_결과);

        // When
        ExtractableResponse<Response> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_요청(token);
        // Then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_결과);

        // When
        ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_목록_삭제_요청(즐겨찾기_생성_결과);
        // Then
        즐겨찾기_삭제됨(즐겨찾기_삭제_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse 강남역, StationResponse 광교역) {
        return null;
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return null;
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(ExtractableResponse<Response> response) {
        return null;
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성_결과) {
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> 즐겨찾기_목록_조회_결과) {
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> 즐겨찾기_삭제_결과) {
    }
}
