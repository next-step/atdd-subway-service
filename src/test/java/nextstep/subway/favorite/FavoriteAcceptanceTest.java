package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.loginRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest 신분당선;

    private String token;

    @BeforeEach
    public void given() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);

        TokenResponse tokenResponse =
            loginRequest(new MemberRequest("test@email", "1234", 29)).as(TokenResponse.class);

        token = tokenResponse.getAccessToken();
    }

    @DisplayName("즐겨찾기 시나리오 테스트")
    @TestFactory
    Stream<DynamicTest> favoriteScenarioTest() {
        // given
        //  지하철역, 노선, 구간, 회원 등록
        //  로그인 상태

        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 광교역.getId());

        return Stream.of(
            dynamicTest("즐겨찾기 생성 성공", createFavoriteSuccess(request)),
            dynamicTest("즐겨찾기 목록 조회", findFavoriteAndTest(request)),
            dynamicTest("즐겨찾기 삭제 성공", deleteFavoriteSuccess(1L))
        );
    }

    private Executable createFavoriteSuccess(FavoriteRequest favoriteRequest) {
        return () -> {
            // when
            ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                           .auth().oauth2(token)
                           .body(favoriteRequest)
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .when().post("/favorites")
                           .then().log().all()
                           .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        };
    }

    private Executable findFavoriteAndTest(FavoriteRequest favoriteRequest) {
        return () -> {
            // when
            ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                           .auth().oauth2(token)
                           .when().get("/favorites")
                           .then().log().all()
                           .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

            FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
            assertThat(favoriteResponse.getSource().getId()).isEqualTo(favoriteRequest.getSource());
        };
    }

    private Executable deleteFavoriteSuccess(Long id) {
        return () -> {
            // when
            ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                           .auth().oauth2(token)
                           .when().delete("/favorites/" + id)
                           .then().log().all()
                           .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        };
    }
}
