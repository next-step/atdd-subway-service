package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_조회;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 양재시민의숲;
    private LineResponse 신분당선;

    private String 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        양재시민의숲 = StationAcceptanceTest.지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);

        // and
        LineRequest lineRequest = LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재시민의숲.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 3);

        // and
        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        사용자 = 토큰_조회(로그인_요청_응답);
    }

    @Test
    void 즐겨찾기_관리() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 양재역);

        // then
        즐겨찾기_생성됨(즐겨찾기_생성_요청_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_실패_요청_응답 = 즐겨찾기_실패_생성_요청(사용자, 강남역, 강남역);

        // then
        즐겨찾기_생성_실패됨(즐겨찾기_생성_실패_요청_응답, "상행역과 하행역이 같으면 등록할 수 없습니다.");

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답 = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = 즐겨찾기_삭제_요청(사용자, 즐겨찾기_생성_요청_응답);

        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_응답);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(source.getId(), target.getId());
        return 생성_요청(FAVORITE_ROOT_PATH, favoriteRequest, accessToken);
    }

    private ExtractableResponse<Response> 즐겨찾기_실패_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        return 즐겨찾기_생성_요청(accessToken, source, target);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response, String errorMessage) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.response().body().asString()).isEqualTo(errorMessage);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return 조회_요청(FAVORITE_ROOT_PATH, accessToken);
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return 삭제_요청(uri, accessToken);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
