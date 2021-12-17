package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.ApiUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private StationResponse 교대역;

    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        //지하철 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_생성_요청("양재역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_생성_요청("남부터미널역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_생성_요청("교대역").as(StationResponse.class);

        //지하철 노선 등록되어 있음
        신분당선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("이호선", "red", 강남역.getId(), 교대역.getId(), 10)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("삼호선", "red", 양재역.getId(), 남부터미널역.getId(), 5)).as(LineResponse.class);

        //지하철 노선에 지하철역 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 교대역, 3);

        //회원 등록되어 있음
        MemberAcceptanceTest.회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);

        //로그인 되어있음
        TokenRequest tokenRequest = new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        ExtractableResponse<Response> 로그인_요청 = AuthAcceptanceTest.로그인_요청(tokenRequest);

        accessToken = 로그인_요청.body().jsonPath().getString("accessToken");
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorites() {

        //즐겨찾기 생성 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 교대역);

        //즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        //즐겨찾기 목록 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_요청(accessToken);

        //즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회_응답됨(findResponse);
        즐겨찾기_목록_조회_포함됨(findResponse, Arrays.asList(createResponse));

        //즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(createResponse, accessToken);

        //즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("즐겨찾기 리스트 조회")
    @Test
    void favoritesTest() {
        //즐겨찾기 생성 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 교대역);

        //즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        //즐겨찾기 목록 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_요청(accessToken);

        //즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회_응답됨(findResponse);
        즐겨찾기_목록_조회_포함됨(findResponse, Arrays.asList(createResponse));
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse upStation, StationResponse downStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(upStation.getId(), downStation.getId());
        return ApiUtils.post("/favorites", accessToken, favoriteRequest);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_요청(String accessToken) {
        return ApiUtils.get("/favorites", accessToken, Collections.emptyMap());
    }

    public static void 즐겨찾기_목록_조회_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_목록_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedIds = createResponses.stream()
                .map(r -> extractId(r.header("Location")))
                .collect(toList());

        List<Long> actualIds = response.body().jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(toList());

        assertThat(expectedIds).containsAll(actualIds);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> createResponse, String accessToken) {
        Long id = extractId(createResponse.header("Location"));
        return ApiUtils.delete(String.format("/favorites/%d", id), accessToken, Collections.emptyMap());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}