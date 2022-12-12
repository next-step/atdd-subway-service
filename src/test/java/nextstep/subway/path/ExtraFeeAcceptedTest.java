package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청을_한다;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 요금 테스트")
public class ExtraFeeAcceptedTest extends AcceptanceTest {

    private LineResponse 이호선;
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private StationResponse 양재시민의숲;

    /**
     * 지하철 노선도
     *
     * 교대역    --- *2호선* ---   강남역 --- *2호선* --- 역삼역 --- *2호선* ---선릉역
     *                            |
     *                         *신분당선*
     *                            |
     *                         양재시민의 숲
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재시민의숲.getId(), 40)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 선릉역.getId(), 20)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재시민의숲, 20);
    }


    @Test
    @DisplayName("로그인 하지 않는 유저가 추가 요금 부담구간이 없는 지하철을 탄다.")
    void notExtraFeeAndNoLogin() {
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 양재시민의숲.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 800);
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청(Long sourceId, Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 상태값이_기대값과_일치하는지_체크한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private void 요금이_일치하는지_체크한다(ExtractableResponse<Response> response, int fee) {
        assertThat(response.jsonPath().getInt("fee")).isEqualTo(fee);
    }
}
