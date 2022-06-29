package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.ui.LineControllerTest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private MemberResponse 회원;
    private String 토큰;
    private RequestSpecification 사용자_요청;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineControllerTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        회원 = MemberAcceptanceTest.회원_등록되어_있음(MemberAcceptanceTest.EMAIL,
                MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        토큰 = AuthAcceptanceTest.로그인_되어_있음(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        사용자_요청 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(토큰);
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
    @Test
    @DisplayName("즐겨찾기 관리")
    void manageFavorite() {
        // when
        final ExtractableResponse<Response> 즐겨찾기_생성_요청 = 즐겨찾기_생성_요청(강남역, 광교역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_요청);

        // when
        final ExtractableResponse<Response> 즐겨찾기_목록_조회_요청 = 즐겨찾기_목록_조회_요청();
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청, 강남역, 광교역);

        // when
        final ExtractableResponse<Response> 즐겨찾기_삭제_요청 = 즐겨찾기_삭제_요청(1);
        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(StationResponse sourceStation, StationResponse targetStation) {
        return 사용자_요청.body(new FavoriteRequest(sourceStation.getId(), targetStation.getId()), ObjectMapperType.JACKSON_2)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return 사용자_요청.when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("source.name")).contains(source.getName()),
                () -> assertThat(response.jsonPath().getList("target.name")).contains(target.getName())
        );
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(long id) {
        return 사용자_요청.when().delete("/favorites/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
