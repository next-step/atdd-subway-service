package nextstep.subway.fare;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.path.PathAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.http.*;

import io.restassured.response.*;
import nextstep.subway.*;
import nextstep.subway.auth.dto.*;
import nextstep.subway.line.dto.*;
import nextstep.subway.member.dto.*;
import nextstep.subway.path.dto.*;
import nextstep.subway.station.dto.*;

public class FareAcceptanceTest extends AcceptanceTest {
    public static final MemberRequest 일반_아이디_생성_요청 = MemberRequest.of("general@email.com", "generalpassword", 19);
    public static final MemberRequest 청소년_아이디_생성_요청 = MemberRequest.of("youth@email.com", "youthpassword", 18);
    public static final MemberRequest 어린이_아이디_생성_요청 = MemberRequest.of("kids@email.com", "kidspassword", 12);

    private LineResponse 삼호선;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    private String 일반토큰;
    private String 청소년토큰;
    private String 어린이토큰;
    private FareCalculator fareCalculator;

    /**
     *                   10
     * 교대역   ----- *2호선* -----  강남역
     * |                            |
     * *3호선*  8                  *신분당선*    8
     * |                            |
     * 남부터미널역 ----- *3호선* -----   양재  --- * 신분당선 * --- 정자역
     *                   5                              4
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
            LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8, 900))
            .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
            .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
            LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 50))
            .as(LineResponse.class);

        회원_생성됨(회원_생성을_요청(일반_아이디_생성_요청));
        회원_생성됨(회원_생성을_요청(청소년_아이디_생성_요청));
        회원_생성됨(회원_생성을_요청(어린이_아이디_생성_요청));

        일반토큰 = 로그인_요청(일반_아이디_생성_요청).as(TokenResponse.class).getAccessToken();
        청소년토큰 = 로그인_요청(청소년_아이디_생성_요청).as(TokenResponse.class).getAccessToken();
        어린이토큰 = 로그인_요청(어린이_아이디_생성_요청).as(TokenResponse.class).getAccessToken();

        지하철_노선에_지하철_구간_등록_요청(삼호선, 교대역, 남부터미널역, 8);
        지하철_노선에_지하철_구간_등록_요청(신분당선, 양재역, 정자역, 4);
    }

    @DisplayName("일반사용자가 추가요금 없는 노선을 8km 이용하면, 금액은 1250원이다")
    @Test
    void calculateFare1() {
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 남부터미널역.getId());
        정상응답_및_요금_확인(response, 일반_아이디_생성_요청.getAge(), 1250);
    }

    @DisplayName("청소년사용자가 추가요금 없는 노선을 8km 이용하면, 금액은 720원이다")
    @Test
    void calculateFare2() {
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 남부터미널역.getId());
        정상응답_및_요금_확인(response, 청소년_아이디_생성_요청.getAge(), 720);
    }

    @DisplayName("어린이사용자가 추가요금 없는 노선을 8km 이용하면, 금액은 450원이다")
    @Test
    void calculateFare3() {
        ExtractableResponse<Response> response = 최단_경로_조회(교대역.getId(), 남부터미널역.getId());
        정상응답_및_요금_확인(response, 어린이_아이디_생성_요청.getAge(), 450);
    }

    @DisplayName("일반사용자가 900원 추가요금 있는 노선을 8km 이용하면, 금액은 2150원이다")
    @Test
    void calculateFare4() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId());
        정상응답_및_요금_확인(response, 일반_아이디_생성_요청.getAge(), 2150);
    }

    @DisplayName("청소년사용자가 900원 추가요금 있는 노선을 8km 이용하면, 금액은 1440원이다")
    @Test
    void calculateFare5() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId());
        정상응답_및_요금_확인(response, 청소년_아이디_생성_요청.getAge(), 1440);
    }

    @DisplayName("어린이사용자가 900원 추가요금 있는 노선을 8km 이용하면, 금액은 900원이다")
    @Test
    void calculateFare6() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 양재역.getId());
        정상응답_및_요금_확인(response, 어린이_아이디_생성_요청.getAge(), 900);
    }

    @DisplayName("일반사용자가 900원 추가요금 있는 노선을 12km 이용하면, 금액은 2250원이다")
    @Test
    void calculateFare7() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 정자역.getId());
        정상응답_및_요금_확인(response, 일반_아이디_생성_요청.getAge(), 2250);
    }

    @DisplayName("청소년사용자가 900원 추가요금 있는 노선을 12km 이용하면, 금액은 1520원이다")
    @Test
    void calculateFare8() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 정자역.getId());
        정상응답_및_요금_확인(response, 청소년_아이디_생성_요청.getAge(), 1520);
    }

    @DisplayName("어린이사용자가 900원 추가요금 있는 노선을 12km 이용하면, 금액은 950원이다")
    @Test
    void calculateFare9() {
        ExtractableResponse<Response> response = 최단_경로_조회(강남역.getId(), 정자역.getId());
        정상응답_및_요금_확인(response, 어린이_아이디_생성_요청.getAge(), 950);
    }

    private void 정상응답_및_요금_확인(ExtractableResponse<Response> response, int age, int expected) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertThat(pathResponse).isNotNull();

        fareCalculator = FareCalculator.from(pathResponse.getTotalDistance(), pathResponse.getLines(), age);
        assertThat(fareCalculator.totalFare()).isEqualTo(Fare.from(expected));
    }
}
