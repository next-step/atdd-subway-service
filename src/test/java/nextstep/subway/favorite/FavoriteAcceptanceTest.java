package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인을_요청_한다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private TokenResponse 사용자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        // And 지하철 노선에 지하철역 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // And 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // And 로그인 되어있음
        사용자 = 로그인을_요청_한다(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 양재역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(getResponse, Collections.singletonList(강남역), Collections.singletonList(양재역));

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        FavoriteRequest params = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();

    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, List<StationResponse> sourceStations, List<StationResponse> targetStations) {
        List<FavoriteResponse> expectedFavoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);

        List<Long> expectedSourceStationIds = convertToIds(sourceStations.stream());
        List<Long> expectedTargetStationIds = convertToIds(targetStations.stream());

        List<Long> resultSourceStationIds = convertToIds(expectedFavoriteResponses
                .stream()
                .map(FavoriteResponse::getSource)
        );

        List<Long> expectedTargetStations = convertToIds(expectedFavoriteResponses
                .stream()
                .map(FavoriteResponse::getTarget)
        );

        assertThat(resultSourceStationIds).containsAll(expectedSourceStationIds);
        assertThat(expectedTargetStations).containsAll(expectedTargetStationIds);
    }

    private static List<Long> convertToIds(Stream<StationResponse> sourceStations) {
        return sourceStations
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}