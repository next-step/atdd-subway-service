package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

//    Feature: 즐겨찾기를 관리한다.
//
//    Scenario: 즐겨찾기를 관리
//    When 즐겨찾기 생성을 요청
//    Then 즐겨찾기 생성됨
//    When 즐겨찾기 목록 조회 요청
//    Then 즐겨찾기 목록 조회됨
//    When 즐겨찾기 삭제 요청
//    Then 즐겨찾기 삭제됨

    private LineResponse 일호선;
    private LineResponse 구호선;
    private LineResponse 오호선;
    private LineResponse 사호선;
    private StationResponse 노량진역;
    private StationResponse 대방역;
    private StationResponse 신길역;
    private StationResponse 여의도역;
    private StationResponse 샛강역;
    private StationResponse 여의나루역;
    private StationResponse 삼각지역;
    private StationResponse 숙대입구역;
    private final String 내_이메일 = "myemail@email.com";
    private final String 내_패스워드 = "mypassword";
    private final int 내_나이 = 22;
    private String 액세스_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        노량진역 = StationAcceptanceTest.지하철역_등록되어_있음("노량진역").as(StationResponse.class);
        대방역 = StationAcceptanceTest.지하철역_등록되어_있음("대방역").as(StationResponse.class);
        신길역 = StationAcceptanceTest.지하철역_등록되어_있음("신길역").as(StationResponse.class);
        여의도역 = StationAcceptanceTest.지하철역_등록되어_있음("여의도역").as(StationResponse.class);
        샛강역 = StationAcceptanceTest.지하철역_등록되어_있음("샛강역").as(StationResponse.class);
        여의나루역 = StationAcceptanceTest.지하철역_등록되어_있음("여의나루역").as(StationResponse.class);
        삼각지역 = StationAcceptanceTest.지하철역_등록되어_있음("삼각지역").as(StationResponse.class);
        숙대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("숙대입구역").as(StationResponse.class);

        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 노량진역, 신길역, 10);
        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-yellow-600", 노량진역, 샛강역, 5);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-purple-600", 신길역, 여의나루역, 12);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-skyblue-600", 삼각지역, 숙대입구역, 5);

        지하철_노선에_지하철역_등록_요청(일호선, 노량진역, 대방역, 7);
        지하철_노선에_지하철역_등록_요청(구호선, 샛강역, 여의도역, 8);
        지하철_노선에_지하철역_등록_요청(오호선, 신길역, 여의도역, 10);

        회원_생성을_요청(내_이메일, 내_패스워드, 내_나이);

        액세스_토큰 = 로그인_요청(내_이메일, 내_패스워드).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기 생성을 요청하면 즐겨찾기가 생성된다.")
    @Test
    void createFavorite() {
        //when
        final ExtractableResponse<Response> 즐겨찾기_생성_결과 = 즐겨찾기_생성_요청(액세스_토큰, 노량진역, 여의도역);

        //then
        즐겨찾기_생성_성공(즐겨찾기_생성_결과);
    }

    private AbstractIntegerAssert<?> 즐겨찾기_생성_성공(final ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(
            final String accessToken,
            final StationResponse source,
            final StationResponse target
    ) {
        final FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }


}