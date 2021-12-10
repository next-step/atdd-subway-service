package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 양재역;
    private LineResponse 신분당선;
    private String 사용자토큰;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        MemberAcceptanceTest.회원_생성을_요청("email@email.com", "password", 20);
        사용자토큰 = MemberAcceptanceTest.로그인_토큰_가져오기(AuthAcceptanceTest.로그인_요청("email@email.com", "password"));
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void favorite() {

        ExtractableResponse<Response> addResponse = 즐겨찾기_등록_요청(사용자토큰, 강남역, 광교역);
        즐겨찾기_등록_됨(addResponse);

        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청(사용자토큰);
        즐겨찾기_목록_조회_됨(listResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_조회요청(사용자토큰, addResponse);
        즐겨찾기_삭제_됨(deleteResponse);
    }

    private void 즐겨찾기_삭제_됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_조회요청(String 사용자토큰, ExtractableResponse<Response> response) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자토큰)
                .when().delete("/favorites/{id}", favoriteResponse.getId())
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회_됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        FavoriteResponse favoriteResponse = Arrays.stream(listResponse.as(FavoriteResponse[].class))
                .collect(Collectors.toList())
                .get(0);

        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(광교역.getName());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String 사용자토큰) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자토큰)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_등록_됨(ExtractableResponse<Response> addResponse) {
        assertThat(addResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String 사용자토큰, StationResponse 강남역, StationResponse 광교역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 광교역.getId());
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자토큰)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
