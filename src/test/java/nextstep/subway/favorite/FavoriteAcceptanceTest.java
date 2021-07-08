package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.AcceptanceDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private final static String EMAIL ="mwkwon@test.com";
    private final static String PASSWORD ="password";

    private StationResponse 서초역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 교대역;
    private LineResponse 이호선;

    private TokenResponse 로그인_정보;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given: 지하철역 등록되어 있음
        서초역 = AcceptanceDataGenerator.지하철역_등록되어_있음("서초역");
        강남역 = AcceptanceDataGenerator.지하철역_등록되어_있음("강남역");
        역삼역 = AcceptanceDataGenerator.지하철역_등록되어_있음("역삼역");
        교대역 = AcceptanceDataGenerator.지하철역_등록되어_있음("교대역");

        // and: 지하철 노선 등록되어 있음
        이호선 = AcceptanceDataGenerator.지하철_노선_등록되어_있음("이호선", "bg-red-600", 서초역, 역삼역, 25);

        // and: 지하철 노선에 지하철역 등록되어 있음
        AcceptanceDataGenerator.지하철_노선에_지하철역_등록_요청(이호선, 서초역, 교대역, 10);
        AcceptanceDataGenerator.지하철_노선에_지하철역_등록_요청(이호선, 교대역, 강남역, 10);

        // and: 회원 등록되어 있음
        AcceptanceDataGenerator.회원_생성을_요청(EMAIL, PASSWORD, 37);
        // and: 로그인 되어있음
        로그인_정보 = AcceptanceDataGenerator.로그인_요청(EMAIL, PASSWORD);
    }

    @Test
    void 즐겨찾기_관리() {
        // given: 지하철 경로를 조회한다
        ExtractableResponse<Response> 경로_조회_결과 = AcceptanceDataGenerator.지하철_경로_조회(서초역, 역삼역, 로그인_정보);
        PathResponse pathResponse = 경로_조회_결과.as(PathResponse.class);
        StationResponse source = pathResponse.getStations().get(0);
        StationResponse target = pathResponse.getStations().get(pathResponse.getStations().size() - 1);

        // when: 조회한 지하철 경로를 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성_요청 = 즐겨찾기_생성_요청(로그인_정보, source, target);
        // then: 즐겨찾기 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성_요청);

        // when: 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> 즐겨찾기_목록_조회 = 즐겨찾기_목록_조회(로그인_정보, source, target);
        // then: 즐겨찾기 목록 조회됨
        List<FavoriteResponse> 즐겨찾기_목록 = 즐겨찾기_목록_조회됨(즐겨찾기_목록_조회, source, target);

        // when: 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 즐겨찾기_삭제 = 즐겨찾기_삭제_요청(로그인_정보, 즐겨찾기_목록.get(0).getId());

        // then: 즐겨 찾기 삭제됨
        즐겨찾기_삭제됨(즐겨찾기_삭제);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse 로그인_정보, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", 로그인_정보.getAccessToken()))
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회(TokenResponse 로그인_정보, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", 로그인_정보.getAccessToken()))
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 로그인_정보, Long id) {
        return RestAssured
                .given().log().all()
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", 로그인_정보.getAccessToken()))
                .when()
                .delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private List<FavoriteResponse> 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = response.body().jsonPath().getList("$", FavoriteResponse.class);
        FavoriteResponse favoriteResponse = favoriteResponses.get(0);
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(source.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(target.getId());
        return favoriteResponses;
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}