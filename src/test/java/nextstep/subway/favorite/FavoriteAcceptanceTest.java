package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 잠실역;
    private LineResponse 이호선;
    private String 사용자;

    /**
     * Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "green", 강남역.getId(), 잠실역.getId(), 10)).as(LineResponse.class);

        ExtractableResponse<Response> createStationResponse = 지하철_노선에_지하철역_등록_요청(이호선, 삼성역, 잠실역, 5);
        지하철_노선에_지하철역_등록됨(createStationResponse);

        ExtractableResponse<Response> createMemberResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createMemberResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(EMAIL, PASSWORD);
        사용자 = 로그인_되어_있음(loginResponse);
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다")
    void 즐겨찾기_관리() {
        // When 즐겨찾기 생성을 요청
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(사용자, 강남역.getId(), 삼성역.getId());
        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(createResponse);

        /*// When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청();
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(findResponse);

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청();
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);*/
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStationId, targetStationId);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
