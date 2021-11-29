package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_발급_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "ungseokchoi";
    private static final String PASSWORD = "ungseok";
    private static final int AGE = 10;

    @Test
    @DisplayName("지하철 노선 즐겨찾기 관리 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 즐겨찾기_관리() {
        // given
        StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8))
                .as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse 인증_토큰 = 토큰_발급_요청(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> 강남_양재_응답 = 즐겨찾기_추가(인증_토큰, FavoriteRequest.of(강남역.getId(), 양재역.getId()));
        ExtractableResponse<Response> 교대_남부터미널역_응답 = 즐겨찾기_추가(인증_토큰, FavoriteRequest.of(교대역.getId(), 남부터미널역.getId()));

        // then
        즐겨찾기_추가됨(강남_양재_응답, 강남역, 양재역);
        즐겨찾기_추가됨(교대_남부터미널역_응답, 교대역, 남부터미널역);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_응답 = 즐겨찾기_목록_조회_요청(인증_토큰);
        List<Long> longs = Arrays.asList(강남역.getId(), 양재역.getId(), 교대역.getId(), 남부터미널역.getId());

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_응답, longs);

        // when
        ExtractableResponse<Response> 삭제_응답 = 즐겨찾기_삭제(인증_토큰, 강남_양재_응답.as(FavoriteResponse.class).getId());

        // then
        즐겨찾기_삭제됨(삭제_응답);
    }

    @Test
    @DisplayName("지하철 노선 즐겨찾기 추가 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 즐겨찾기_생성_강남_남부터미널() {
        // given
        StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8))
                .as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse 인증_토큰 = 토큰_발급_요청(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> actual = 즐겨찾기_추가(인증_토큰, FavoriteRequest.of(강남역.getId(), 양재역.getId()));

        // then
        즐겨찾기_추가됨(actual, 강남역, 양재역);
    }

    @Test
    @DisplayName("지하철 노선 즐겨찾기 삭제 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 즐겨찾기_삭제_강남_남부터미널() {
        // given
        StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        LineResponse 신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8))
                .as(LineResponse.class);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenResponse 인증_토큰 = 토큰_발급_요청(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);
        FavoriteResponse 강남_양재 = 즐겨찾기_추가(인증_토큰, FavoriteRequest.of(강남역.getId(), 양재역.getId())).as(FavoriteResponse.class);
        FavoriteResponse 교대_남부터미널 = 즐겨찾기_추가(인증_토큰, FavoriteRequest.of(교대역.getId(), 남부터미널역.getId())).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> actual = 즐겨찾기_삭제(인증_토큰, 강남_양재.getId());

        // then
        즐겨찾기_삭제됨(actual);
    }

    public static ExtractableResponse<Response> 즐겨찾기_추가(TokenResponse token, FavoriteRequest request) {
        return postAuth("/favorites", token, request);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(TokenResponse token, Long id) {
        return deleteAuth("/favorites/{id}", token, id);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse token) {
        return getAuth("/favorites", token);
    }

    private void 즐겨찾기_추가됨(ExtractableResponse<Response> actual, StationResponse source, StationResponse target) {
        응답_CREATE(actual);

        FavoriteResponse response = actual.as(FavoriteResponse.class);
        assertAll(
                () -> assertThat(response.getSource().getId()).isEqualTo(source.getId()),
                () -> assertThat(response.getTarget().getId()).isEqualTo(target.getId())
        );
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> actual) {
        응답_NO_CONTENT(actual);
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> actual, List<Long> expected) {
        응답_OK(actual);

        List<Long> stations = new ArrayList<>();
        actual.jsonPath()
                .getList(".", FavoriteResponse.class)
                .forEach(it -> {
                    stations.add(it.getSource().getId());
                    stations.add(it.getTarget().getId());
                });

        assertThat(stations).containsAll(expected);
    }

}