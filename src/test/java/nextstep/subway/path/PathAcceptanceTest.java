package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.common.domain.DiscountByAge;
import nextstep.subway.common.domain.SubwayFare;
import nextstep.subway.common.domain.SurchargeByLine;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.auth.application.AuthServiceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
@ExtendWith(MockitoExtension.class)
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 고속터미널역;
    private String 일반사용자토큰;



    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        고속터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
        이호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10));
        삼호선 = LineAcceptanceTest.지하철_노선_등록_요청(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 5));

        지하철역_노선에_지하철역_추가(삼호선, 고속터미널역, 교대역, 3);

        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenRequest request = new TokenRequest(EMAIL, PASSWORD);
        일반사용자토큰 = 로그인_되어_있음(request);
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findBestPathTest() {
        //given
        Map<String, Long> pathRequestMap = createPathRequestMap(교대역.getId(), 남부터미널역.getId());

        //when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(일반사용자토큰, pathRequestMap);

        //then
        최단_경로_조회됨(response, 5);
    }

    @DisplayName("두 역의 최단 거리 경로와 요금을 조회")
    @Test
    void findBestPathWithFareTest() {
        //given
        Map<String, Long> pathRequestMap = createPathRequestMap(교대역.getId(), 남부터미널역.getId());
        //when
        ExtractableResponse<Response> 기본요금_노선요청_맵 = 최단_경로_조회_요청(일반사용자토큰, pathRequestMap);
        //then
        최단_경로_조회됨(기본요금_노선요청_맵, 5);
        요금_조회됨(기본요금_노선요청_맵, SubwayFare.BASIC_FARE);

        //given
        Map<String, Long> 노선_할증_노선요청_맵 = createPathRequestMap(강남역.getId(), 양재역.getId());
        //when
        ExtractableResponse<Response> chargedByLineresponse = 최단_경로_조회_요청(일반사용자토큰, 노선_할증_노선요청_맵);
        //then
        최단_경로_조회됨(chargedByLineresponse, 10);
        요금_조회됨(chargedByLineresponse, SubwayFare.BASIC_FARE.add(SurchargeByLine.SINBUNDANG.amount()));

        //given
        Map<String, Long> 노선_거리_할증_노선요청_맵 = createPathRequestMap(교대역.getId(), 양재역.getId());
        //when
        ExtractableResponse<Response> chargedByLineAndDistanceResponse = 최단_경로_조회_요청(일반사용자토큰, 노선_거리_할증_노선요청_맵);
        //then
        최단_경로_조회됨(chargedByLineAndDistanceResponse, 20);
        요금_조회됨(chargedByLineAndDistanceResponse, SubwayFare.BASIC_FARE.add(SurchargeByLine.SINBUNDANG.amount()).add(BigDecimal.valueOf(200)));

        //given
        회원_등록되어_있음("child@email.com", "password", 10);
        TokenRequest request = new TokenRequest("child@email.com", "password");
        String 어린이_토큰 = 로그인_되어_있음(request);
        Map<String, Long> 노선_거리_할증_어린이할인_노선요청_맵 = createPathRequestMap(교대역.getId(), 양재역.getId());
        //when
        ExtractableResponse<Response> chargedByLineAndDistanceDiscountByAgeResponse = 최단_경로_조회_요청(어린이_토큰, 노선_거리_할증_어린이할인_노선요청_맵);
        //then
        최단_경로_조회됨(chargedByLineAndDistanceDiscountByAgeResponse, 20);
        BigDecimal expectFare = (SubwayFare.BASIC_FARE  //기본요금
                .add(SurchargeByLine.SINBUNDANG.amount())    //노선추가금
                .add(BigDecimal.valueOf(200))   //거리추가금
                .subtract(DiscountByAge.CHILD.getExcludedPrice()))  //연령 할인금액
                .multiply(DiscountByAge.CHILD.getPayoutRate()) //연령 할인율
                .setScale(0, RoundingMode.HALF_UP); //소수점 반올림
        요금_조회됨(chargedByLineAndDistanceDiscountByAgeResponse, expectFare);



    }

    private void 요금_조회됨(ExtractableResponse<Response> response, BigDecimal expectFare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getPaymentFare()).isEqualTo(expectFare);
    }


    private Map<String, Long> createPathRequestMap(Long sourceId, Long targetId) {
        Map<String, Long> pathRequestMap = new HashMap<>();
        pathRequestMap.put("source", sourceId);
        pathRequestMap.put("target", targetId);
        return pathRequestMap;
    }

    private static ExtractableResponse<Response> 최단_경로_조회_요청(String 사용자토큰, Map<String, Long> pathRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자토큰)
                .queryParams(pathRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회됨(ExtractableResponse<Response> response, int expectDistance) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(expectDistance);
    }


}
