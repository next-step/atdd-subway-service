package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private TokenResponse tokenResponse;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private StationResponse 잠실역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 100);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 10);

        LineRequest lineRequest2 = new LineRequest("이호선", "green", 잠실역.getId(), 강남역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest2).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 잠실역, 양재역, 10);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        ExtractableResponse<Response> response = 즐겨찾기_생성을_요청(잠실역.getId(), 판교역.getId());
        즐겨찾기_생성됨(response);

        즐겨찾기_생성을_요청(강남역.getId(), 양재역.getId());
        ExtractableResponse<Response> response2 =즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회됨(response2,2, Arrays.asList(잠실역.getId(), 강남역.getId()), Arrays.asList(판교역.getId(), 양재역.getId()));

        ExtractableResponse<Response> response3 = 즐겨찾기_삭제_요청(response);
        즐겨찾기_삭제됨(response3);

    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long sourceId, Long targetId) {
        FavoriteRequest request = new FavoriteRequest(sourceId, targetId);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int size, List<Long> sourceResult, List<Long> targetResult)  {
        List<FavoriteResponse> favoriteResponseList = response.jsonPath().getList(".", FavoriteResponse.class)
                .stream()
                .collect(Collectors.toList());

        List<Long> sourceStations = response.jsonPath().getList(".", FavoriteResponse.class)
                .stream()
                .map(FavoriteResponse::getSource)
                .map(f -> f.getId())
                .collect(Collectors.toList());

        List<Long> targetStations = response.jsonPath().getList(".", FavoriteResponse.class)
                .stream()
                .map(FavoriteResponse::getTarget)
                .map(f -> f.getId())
                .collect(Collectors.toList());

        assertThat(favoriteResponseList).hasSize(size);
        assertThat(sourceStations).containsAll(sourceResult);
        assertThat(targetStations).containsAll(targetResult);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}