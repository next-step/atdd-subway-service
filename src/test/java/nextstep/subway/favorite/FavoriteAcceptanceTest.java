package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthTestFixture.로그인을_요청;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_생성됨;
import static nextstep.subway.favorite.FavoriteTestFixture.즐겨찾기_생성을_요청;
import static nextstep.subway.line.acceptance.LineSectionTestFixture.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.line.acceptance.LineTestFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberTestFixture.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String EMAIL = "email@email.com";
    private String PASSWORD = "password";
    private String NEW_EMAIL = "newemail@email.com";
    private String NEW_PASSWORD = "newpassword";
    private int AGE = 20;
    private int NEW_AGE = 21;
    private TokenResponse token;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //Given 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        //And 지하철 노선 등록되어 있음
        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        //And 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        //And 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        //And 로그인 되어있음
        token = 로그인을_요청(EMAIL, PASSWORD).as(TokenResponse.class);
        favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
    }


    @DisplayName("즐겨찾기를 관리")
    @Test
    void favorite_scenario() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(token, favoriteRequest);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> searchResponse = 즐겨찾기_목록_조회_요청(token);
        즐겨찾기_목록_조회됨(searchResponse);

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, "1");
        즐겨찾기_삭제됨(deleteResponse);
    }


}
