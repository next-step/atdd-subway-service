package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private TokenResponse 사용자;

    private ExtractableResponse<Response> 즐겨찾기;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        지하철_노선_등록되어_있음("2호선", "bg-red-600", 강남역, 역삼역, 10);
        회원_생성되어_있음("heowc1992@gmail.com", "password", 31);
        사용자 = 로그인_됨("heowc1992@gmail.com", "password").as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리")
    @TestFactory
    Stream<DynamicNode> favorite() {
        return Stream.of(
                dynamicTest("즐겨찾기를 생성한다.", () -> {
                    즐겨찾기 = 즐겨찾기_생성_요청(사용자, 강남역, 역삼역);

                    즐겨찾기_생성됨(즐겨찾기);
                }),
                dynamicTest("즐겨찾기 목록을 조회한다.", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(사용자);

                    즐겨찾기_조회됨(response, 강남역, 역삼역);
                }),
                dynamicTest("즐거찾기를 삭제한다.", () -> {
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(사용자, 즐겨찾기);

                    즐겨찾기_삭제됨(response);
                })
        );
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, StationResponse sourceStation,
                                                             StationResponse targetStation) {
        FavoriteRequest request = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse,
                                                             ExtractableResponse<Response> favoriteResponse) {
        String uri = favoriteResponse.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response,
                                 StationResponse source, StationResponse target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponses).hasSize(1);

        FavoriteResponse favoriteResponse = favoriteResponses.get(0);
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(source.getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(target.getName());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
