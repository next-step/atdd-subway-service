package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private MemberResponse 사용자;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남강역;
    private StationResponse 대교역;
    private StationResponse 널미터부남역;
    private StationResponse 남부터미널역;
    private StationResponse 노원역;
    private StationResponse 군자역;

    /**
     * 교대역    --- *2호선* ---     강남역 --- *2호선* ---   대교역
     * |            30                |         11
     * |                          *신분당선* 17
     * |                              |
     * |                            남강역
     * |                              |
     * *3호선* 43                  *신분당선* 10
     * |                              |
     * 남부터미널역  --- *3호선* ---   양재역 --- *3호선* --- 널미터부남역
     *                  2                       21
     *
     *
     * 노원역 --- *7호선* --- 군자역
     *             77
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남강역 = 지하철역_등록되어_있음("남강역").as(StationResponse.class);
        대교역 = 지하철역_등록되어_있음("대교역").as(StationResponse.class);
        노원역 = 지하철역_등록되어_있음("노원역").as(StationResponse.class);
        군자역 = 지하철역_등록되어_있음("군자역").as(StationResponse.class);
        널미터부남역 = 지하철역_등록되어_있음("널미터부남역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 27))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(
                new LineRequest("칠호선", "bg-red-600", 노원역.getId(), 군자역.getId(), 77))
                .as(LineResponse.class);

        // And 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 43);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 남강역, 17);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 대교역, 11);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 널미터부남역, 21);

        // And 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // And 로그인 되어있음
        사용자 = 로그인_되어_있음(EMAIL, PASSWORD).as(MemberResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // When
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 남부터미널역);
        // Then
        즐겨찾기_생성됨(createResponse);

        // When
        ExtractableResponse<Response> selectResponse = 즐겨찾기_목록_조회_요청();
        // Then
        즐겨찾기_목록_조회됨(selectResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청();
        // Then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(MemberResponse loginUser, StationResponse sourceStation, StationResponse targetStation) {
        return null;
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return null;
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> selectResponse) {
        assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청() {
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}