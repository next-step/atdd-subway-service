package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_구간_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 요금 인수 테스트")
public class PathFareAcceptanceTest extends AcceptanceTest {
    private StationResponse 송정역;
    private StationResponse 김포공항역;
    private StationResponse 공항시장역;
    private StationResponse 신방화역;
    private StationResponse 마곡나루역;
    private StationResponse 계양역;
    private StationResponse 귤현역;
    private StationResponse 박촌역;

    /**
     * 김포공항 ---- (7) --- 송정      * (5호선)
     * |
     * 마곡나루 - (5) - 신방화-- (2) - 공항시장 - (3) - 김포공항                       * (9호선)
     * |                                          |
     * 마곡나루 -------------- (10) --------------- 김포공항 --- (15) --- 계양      * (공항철도: 추가운임 900원)
     * |
     * 박촌 ------------ (20) ---------- 귤현 ---- (10) ---- 계양      * (인천1: 추가운임 500원)
     *
     * [거리<=10km, 환승:1] - 1250원
     * 송정 -> 공항시장 : (송정-7-김포공항-3-공항시장) 10
     *
     * [10km<거리<=50km, 환승:1, 공항철도:900] - 2350원 (거리추가:200, 추가운임노선:900)
     * 신방화 -> 계양 : (신방화-5-마곡나루-10-김포공항-15-계양) 30 / (신방화-2-공항시장-3-김포공항-15-계양) 20
     *
     * [50km<거리, 환승:1, 인천1:500, 공항철도:900] - 2750원 (거리추가:600, 추가운임노선:900)
     * 박촌 -> 마곡나루 : (박촌-20-귤현-10-계양-15-김포공항-10-마곡나루) 55
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        송정역 = 지하철역_생성_요청("송정역").as(StationResponse.class);
        김포공항역 = 지하철역_생성_요청("김포공항역").as(StationResponse.class);
        공항시장역 = 지하철역_생성_요청("공항시장역").as(StationResponse.class);
        신방화역 = 지하철역_생성_요청("신방화역").as(StationResponse.class);
        마곡나루역 = 지하철역_생성_요청("마곡나루역").as(StationResponse.class);
        계양역 = 지하철역_생성_요청("계양역").as(StationResponse.class);
        귤현역 = 지하철역_생성_요청("귤현역").as(StationResponse.class);
        박촌역 = 지하철역_생성_요청("박촌역").as(StationResponse.class);

        LineResponse 오호선 = 지하철_노선_등록되어_있음("오호선", "bg-violet-600", 0, 송정역, 김포공항역, 7);
        LineResponse 구호선 = 지하철_노선_등록되어_있음("구호선", "bg-gold-600", 0, 김포공항역, 공항시장역, 3);
        지하철_구간_등록됨(구호선, 공항시장역, 신방화역, 2);
        지하철_구간_등록됨(구호선, 신방화역, 마곡나루역, 5);

        LineResponse 공항철도 = 지하철_노선_등록되어_있음("공항철도", "bg-royal_blue-600", 900, 마곡나루역, 김포공항역, 10);
        지하철_구간_등록됨(공항철도, 김포공항역, 계양역, 15);

        LineResponse 인천1 = 지하철_노선_등록되어_있음("인천1", "bg-light_blue-600", 500, 계양역, 귤현역, 10);
        지하철_구간_등록됨(인천1, 귤현역, 박촌역, 20);

        회원_등록됨(회원_생성_요청("basic@gmail.com", "pa**@@rd", 19));
        회원_등록됨(회원_생성_요청("youth@gmail.com", "pa**@@rd", 18));
        회원_등록됨(회원_생성_요청("child@gmail.com", "pa**@@rd", 12));
        회원_등록됨(회원_생성_요청("baby@gmail.com", "pa**@@rd", 5));
        회원_등록됨(회원_생성_요청("elder@gmail.com", "pa**@@rd", 65));
    }

    @Test
    @DisplayName("추가 요금 정상 기능")
    void normalExtraFareScenario() {
        ExtractableResponse<Response> response10km = 최단_경로_조회_요청(송정역, 공항시장역);
        최단_경로_조회됨(response10km);
        최단_경로_구간_목록_일치됨(response10km, Arrays.asList(송정역, 김포공항역, 공항시장역));
        최단_경로_거리_일치됨(response10km, 10);
        운임_요금_일치됨(response10km, 1250); // 1250원 (기본요금)

        ExtractableResponse<Response> response20km = 최단_경로_조회_요청(신방화역, 계양역);
        최단_경로_조회됨(response20km);
        최단_경로_구간_목록_일치됨(response20km, Arrays.asList(신방화역, 공항시장역, 김포공항역, 계양역));
        최단_경로_거리_일치됨(response20km, 20);
        운임_요금_일치됨(response20km, 2350); // 2350원 (거리추가:200, 추가운임노선:900)

        ExtractableResponse<Response> response55km = 최단_경로_조회_요청(박촌역, 마곡나루역);
        최단_경로_조회됨(response55km);
        최단_경로_구간_목록_일치됨(response55km, Arrays.asList(박촌역, 귤현역, 계양역, 김포공항역, 마곡나루역));
        최단_경로_거리_일치됨(response55km, 55);
        운임_요금_일치됨(response55km, 2750); // 2750원 (거리추가:600, 추가운임노선:900)
    }

    @Test
    @DisplayName("할인 요금 정상 기능")
    void normalDiscountFareScenario() {
        TokenResponse basicToken = 로그인_요청("basic@gmail.com", "pa**@@rd").as(TokenResponse.class);
        ExtractableResponse<Response> basic10km = 최단_경로_조회_요청(basicToken, 송정역, 공항시장역);
        최단_경로_조회됨(basic10km);
        운임_요금_일치됨(basic10km, 1250); // 1250원 (기본요금)

        TokenResponse youthToken = 로그인_요청("youth@gmail.com", "pa**@@rd").as(TokenResponse.class);
        ExtractableResponse<Response> youth10km = 최단_경로_조회_요청(youthToken, 송정역, 공항시장역);
        최단_경로_조회됨(youth10km);
        운임_요금_일치됨(youth10km, 720); // 720원 (기본요금, 공제:-350원, 할인:20%)

        TokenResponse childToken = 로그인_요청("child@gmail.com", "pa**@@rd").as(TokenResponse.class);
        ExtractableResponse<Response> chile10km = 최단_경로_조회_요청(childToken, 송정역, 공항시장역);
        최단_경로_조회됨(chile10km);
        운임_요금_일치됨(chile10km, 450); // 450원 (기본요금, 공제:-350원, 할인:50%)
    }

    public static void 운임_요금_일치됨(ExtractableResponse<Response> response, int excepted) {
        assertThat(response.jsonPath().getObject("money", Integer.class))
                .isEqualTo(excepted);
    }
}
