package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private TokenResponse tokenResponse;

    public static String source = "1";
    public static String target = "2";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);

        tokenResponse = 로그인_요청됨(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기 생성을 요청 테스트")
    void createFavoriteTest() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        ExtractableResponse<Response> response = 즐겨찾기_조회됨(favoriteRequest);

        즐겨찾기_생성_검증됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_조회됨(FavoriteRequest favoriteRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}