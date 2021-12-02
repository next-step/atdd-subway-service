package nextstep.subway.fee;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthFactory;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.FavoriteAcceptanceTest;
import nextstep.subway.line.acceptance.LineFactory;
import nextstep.subway.line.acceptance.LineSectionFactory;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.PathFactory;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationFactory;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class FeeAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 수서역;

    private static final MemberRequest adult = new MemberRequest("aa1@email.com", "pw1", 23);
    private static final MemberRequest child = new MemberRequest("aa2@email.com", "pw2", 11);
    private static final MemberRequest teenager = new MemberRequest("aa3@email.com", "pw3", 14);

    private static String adultToken;
    private static String childToken;
    private static String teenagerToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createResponse;
        ExtractableResponse<Response> tokenResponse;

        강남역 = StationFactory.지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = StationFactory.지하철역_생성_요청("양재역").as(StationResponse.class);
        교대역 = StationFactory.지하철역_생성_요청("교대역").as(StationResponse.class);
        남부터미널역 = StationFactory.지하철역_생성_요청("남부터미널역").as(StationResponse.class);
        수서역 = StationFactory.지하철역_생성_요청("수서역").as(StationResponse.class);
        
        신분당선 = LineFactory.지하철_노선_생성_요청(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 200)
        ).as(LineResponse.class);
        이호선 = LineFactory.지하철_노선_생성_요청(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 100)
        ).as(LineResponse.class);
        삼호선 = LineFactory.지하철_노선_생성_요청(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 0)
        ).as(LineResponse.class);

        LineSectionFactory.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        ExtractableResponse<Response> stationCreateResponse = LineSectionFactory.지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 수서역, 15);

        createResponse = AuthFactory.회원_생성을_요청(adult);
        FavoriteAcceptanceTest.회원_생성됨(createResponse);

        createResponse = AuthFactory.회원_생성을_요청(child);
        FavoriteAcceptanceTest.회원_생성됨(createResponse);

        createResponse = AuthFactory.회원_생성을_요청(teenager);
        FavoriteAcceptanceTest.회원_생성됨(createResponse);

        tokenResponse = AuthFactory.토큰을_요청함(new TokenRequest(adult.getEmail(), adult.getPassword()));
        adultToken = tokenResponse.as(TokenResponse.class).getAccessToken();

        tokenResponse = AuthFactory.토큰을_요청함(new TokenRequest(child.getEmail(), child.getPassword()));
        childToken = tokenResponse.as(TokenResponse.class).getAccessToken();

        tokenResponse = AuthFactory.토큰을_요청함(new TokenRequest(teenager.getEmail(), teenager.getPassword()));
        teenagerToken = tokenResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("성인요금이 적용된 이동경로 요금을 조회한다")
    @Test
    void getAdultFee() {

        ExtractableResponse<Response> response = PathFactory.최단_경로를_조회(강남역.getId(), 남부터미널역.getId(), adultToken);
        요금을_조회하여_비교(response, 1450);
    }

    @DisplayName("청소년요금이 적용된 이동경로 요금을 조회한다")
    @Test
    void getTeenagerFee() {

        ExtractableResponse<Response> response = PathFactory.최단_경로를_조회(강남역.getId(), 남부터미널역.getId(), teenagerToken);
        요금을_조회하여_비교(response, (int) ((1450 - 350) * 0.8) );
    }

    @DisplayName("어린이요금이 적용된 이동경로 요금을 조회한다")
    @Test
    void getChildFee() {

        ExtractableResponse<Response> response = PathFactory.최단_경로를_조회(강남역.getId(), 남부터미널역.getId(), childToken);
        요금을_조회하여_비교(response, (int) ((1450 - 350) * 0.5) );
    }


    @DisplayName("(추가요금 발생)성인요금이 적용된 이동경로 요금을 조회한다")
    @Test
    void getAdultAdditionalFee() {

        ExtractableResponse<Response> response = PathFactory.최단_경로를_조회(강남역.getId(), 수서역.getId(), adultToken);
        요금을_조회하여_비교(response, 1550);
    }


    private void 요금을_조회하여_비교(ExtractableResponse response, int fee) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getFee()).isEqualTo(fee);
    }
}
