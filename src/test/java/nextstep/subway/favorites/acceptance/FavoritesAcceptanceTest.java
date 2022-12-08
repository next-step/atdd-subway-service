package nextstep.subway.favorites.acceptance;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_후_토큰_조회;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 판교역;
    private StationResponse 정자역;
    private LineResponse 신분당선;

    private String EMAIL = "test@test.com";
    private String PASSWORD = "test";
    private int AGE = 20;

    private String token;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600",
                강남역.getId(), 정자역.getId(), 10))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 판교역, 정자역, 5);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_후_토큰_조회(EMAIL, PASSWORD);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 정보를 관리한다.")
    @Test
    void manageFavorite() {
        //given
        Long favoritesRegisterId1 = 강남역.getId();
        Long favoritesRegisterId2 = 정자역.getId();

        // when 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(favoritesRegisterId1, favoritesRegisterId2);
        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        // when 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findListResponse = 즐겨찾기_목록_조회_요청();
        // then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(findListResponse, favoritesRegisterId1, favoritesRegisterId2);

        // when 즐겨찾기 삭제 요청
        Long deleteId = 즐겨찾기_목록_추출(findListResponse).get(0).getId();
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(deleteId);
        // then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 다른 id로 로그인하여 목록조회
     *     Then 이전 id로 등록된 즐겨찾기 조회 불가
     */
    @DisplayName("즐겨찾기 등록 후 다른 사람 id로 보이는지 확인")
    @Test
    void registerFavoritesAndFindAnotherPersonTest() {
        //given
        Long test1FavoritesSourceStationId = 강남역.getId();
        Long test1FavoritesTargetStationId = 정자역.getId();
        String test2Email = "test2@test.com";

        // when 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(test1FavoritesSourceStationId, test1FavoritesTargetStationId);
        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        // when 다른 id로 로그인하여 목록조회
        회원_생성을_요청(test2Email, PASSWORD, AGE);
        token = 로그인_후_토큰_조회(test2Email, PASSWORD);
        ExtractableResponse<Response> retrieveAnotherIdResponse = 즐겨찾기_목록_조회_요청();
        // then 즐겨찾기 목록조회시 이전 id로 등록된 즐겨찾기 조회 불가
        assertThat(즐겨찾기_목록_추출(retrieveAnotherIdResponse).isEmpty()).isTrue();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long source, Long target) {
        FavoritesRequest favoritesRequest = new FavoritesRequest(source, target);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoritesRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/favorites/"+id)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long stationId1, Long stationId2) {
        List<FavoritesResponse> favoritesResponses = 즐겨찾기_목록_추출(response);
        assertThat(favoritesResponses.size()).isEqualTo(1);
        assertThat(favoritesResponses.get(0).getId()).isNotNull();
        assertThat(favoritesResponses.get(0).getStations().stream()
                .map(StationResponse::getId).collect(Collectors.toList()))
                .contains(stationId1, stationId2);
    }

    private List<FavoritesResponse> 즐겨찾기_목록_추출(ExtractableResponse<Response> response) {
        return response.as(new TypeRef<List<FavoritesResponse>>() {}.getType());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}