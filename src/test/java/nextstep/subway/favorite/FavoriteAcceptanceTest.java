package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.domain.favorite.domain.Favorite;
import nextstep.subway.domain.favorite.dto.FavoriteRequest;
import nextstep.subway.domain.favorite.dto.FavoriteResponse;
import nextstep.subway.domain.line.acceptance.LineAcceptanceTest;
import nextstep.subway.domain.line.dto.LineRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.member.MemberAcceptanceTest;
import nextstep.subway.domain.path.PathAcceptanceTest;
import nextstep.subway.domain.station.StationAcceptanceTest;
import nextstep.subway.domain.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 삼호선;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 사용자;
    private final String EMAIL = "hongji3354@gmail.com";
    private final String PASSWORD = "hongji3354";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-200", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        PathAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        회원_등록되어_있음(EMAIL, PASSWORD, 25);

        사용자 = 로그인_되어있음(EMAIL, PASSWORD);
    }

    private String 로그인_되어있음(String email, String password) {
        return MemberAcceptanceTest.토큰_발급(email, password);
    }

    private void 회원_등록되어_있음(String email, String password, int age) {
        MemberAcceptanceTest.회원_생성을_요청(email, password, age);
    }

    @Test
    @DisplayName("즐겨찾기를 관리")
    void manageFavorite() {
        // given
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성을_요청(사용자, 교대역, 양재역);
        // when
        즐겨찾기_생성됨(즐겨찾기_생성_요청_응답);

        // given
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답 = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_응답);

    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답) {
        List<FavoriteResponse> favoriteResponse = 즐겨찾기_목록_조회_요청_응답.jsonPath().getList("$", FavoriteResponse.class);

        assertAll(() -> {
            assertThat(즐겨찾기_목록_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(favoriteResponse.size()).isOne();
            assertThat(favoriteResponse.get(0).getSource().getId()).isEqualTo(교대역.getId());
            assertThat(favoriteResponse.get(0).getTarget().getId()).isEqualTo(양재역.getId());
        });
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String accessToken, StationResponse source, StationResponse target) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source.getId(), target.getId()))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}