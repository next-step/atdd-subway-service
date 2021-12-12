package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.RestAssuredApi;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 서초역;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private LineResponse 이호선;

    private String email = "abc123@gmail.com";
    private String password = "pa**@@rd";
    private int age = 20;

    /**
     * 2호선: 서초 - 교대 - 강남 - 역삼 - 선릉
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        서초역 = 지하철역_생성_요청("서초역").as(StationResponse.class);
        교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class);
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        역삼역 = 지하철역_생성_요청("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_생성_요청("선릉역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("이호선", "bg-green-600", 서초역.getId(), 교대역.getId(), 3);
        이호선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        지하철_구간_등록됨(지하철_구간_등록_요청(이호선, 교대역, 강남역, 3));
        지하철_구간_등록됨(지하철_구간_등록_요청(이호선, 강남역, 역삼역, 3));
        지하철_구간_등록됨(지하철_구간_등록_요청(이호선, 역삼역, 선릉역, 3));

        회원_등록됨(회원_생성_요청(email, password, age));
    }

    @Test
    @DisplayName("즐겨찾기 정상 기능")
    void normalScenario() {
        TokenResponse token = 로그인_요청(email, password).as(TokenResponse.class);
        String createdLocationUri = 즐겨찾기_생성됨(즐겨찾기_생성을_요청(token, 서초역, 역삼역));
        즐겨찾기_생성됨(즐겨찾기_생성을_요청(token, 선릉역, 강남역));

        ExtractableResponse<Response> response1 = 즐겨찾기_목록_조회_요청(token);
        즐겨찾기_목록_조회됨(response1);
        즐겨찾기_목록_일치됨(response1, Arrays.asList("서초역", "선릉역"), Arrays.asList("역삼역", "강남역"));

        즐겨찾기_삭제됨(즐겨찾기_삭제_요청(createdLocationUri, token));
        ExtractableResponse<Response> response2 = 즐겨찾기_목록_조회_요청(token);
        즐겨찾기_목록_조회됨(response2);
        즐겨찾기_목록_일치됨(response2, Arrays.asList("선릉역"), Arrays.asList("강남역"));
    }

    @Test
    @DisplayName("즐겨찾기 예외 발생")
    void exceptionScenario() {
        TokenResponse token = new TokenResponse("invalidToken");
        즐겨찾기_생성_실패됨(즐겨찾기_생성을_요청(token, 서초역, 역삼역));

        즐겨찾기_목록_조회_실패됨(즐겨찾기_목록_조회_요청(token));
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse token, StationResponse departure, StationResponse arrival) {
        FavoritePathRequest request = new FavoritePathRequest(departure.getId(), arrival.getId());
        return RestAssuredApi.authPost("/favorites", token.getAccessToken(), request);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
        return RestAssuredApi.authGet("/favorites", token.getAccessToken());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String uri, TokenResponse token) {
        return RestAssuredApi.authDelete(uri, token.getAccessToken());
    }

    private String 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.header("Location");
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_목록_일치됨(ExtractableResponse<Response> response, List<String> sources, List<String> targets) {
        assertThat(response.jsonPath().getList("source.name", String.class))
                .isEqualTo(sources);
        assertThat(response.jsonPath().getList("target.name", String.class))
                .isEqualTo(targets);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 즐겨찾기_목록_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}