package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.ACCESS_TOKEN_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 역삼역;
    private String 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        // 지하철 노선 등록되어 있음
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 양재역.getId(), 정자역.getId(), 10);
        LineRequest lineRequest2 = new LineRequest("이호선", "bg-green-400", 강남역.getId(), 역삼역.getId(), 5);
        // 지하철 노선에 지하철역 등록되어 있음
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest1);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest2);
        // 회원 등록되어 있음
        회원_등록됨(EMAIL, PASSWORD, AGE);
        // 로그인 되어 있음
        토큰 = ACCESS_TOKEN_요청(EMAIL, PASSWORD).as(TokenResponse.class)
                .getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void favoriteManageTest() {
        // when 즐겨찾기 생성 요청
        ExtractableResponse<Response> savedResponse = 즐겨찾기_생성_요청(양재역.getId(), 정자역.getId());

        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(savedResponse);

        // when 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청();

        // then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(findResponse);

        // when 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(savedResponse);

        // then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("지하철 역에 등록되지 않는 지하철 역을 추가했을 경우")
    @Test
    void notFoundStationByLine() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(양재역.getId(), 10L);

        // then
        즐겨찾기_생성_실패됨(response);
    }

    @DisplayName("유효하지 않는 경로를 선택했을 경우")
    @Test
    void invalidLineSelect() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(양재역.getId(), 역삼역.getId());

        // then
        즐겨찾기_생성_실패됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
        PathRequest saveRequest = new PathRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰)
                .body(saveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }


    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰)
                .when().delete(uri)
                .then().log().all().extract();
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.BAD_REQUEST);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.CREATED);
        assertThat(response.header("Location"))
                .isEqualTo("/favorites/" + response.as(FavoriteResponse.class).getId());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.OK);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        요청_결과_검증(response, HttpStatus.NO_CONTENT);
    }
}