package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.amount.domain.Amount;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.LineId;
import nextstep.subway.station.domain.StationId;

@DisplayName("지하철 경로 조회 - 연령별 요금 할인")
public class PathAgeDiscountAcceptanceTest extends AcceptanceTest {
    private StationId 왕십리;
    private StationId 신당;
    private StationId DDP;

    /**
     * Background
     * Given 지하철 역과 노선 구역이 등록되어 있음
     * 2호선: 왕십리 - 신당 - DDP (추가요금 200원)
     */
    @BeforeEach
    void setup() {
        super.setUp();

        // given
        왕십리 = 지하철역_ID_추출(지하철역_등록되어_있음("왕십리"));
        신당 = 지하철역_ID_추출(지하철역_등록되어_있음("신당"));
        DDP = 지하철역_ID_추출(지하철역_등록되어_있음("DDP"));

        지하철_노선과_구역_등록되어_있음_2호선();
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * And 회원 등록되어 있음
     * And 토큰 발급(로그인)되어 있음
     * When 로그인(19세 이상) 상태로 최단 경로 조회
     * Then 최단 경로 조회 성공
     * And 기본 요금 확인
     */
    @DisplayName("로그인(19세 이상) 상태로 최단 경로 조회")
    @Test
    void no_discount() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 20);
        String 토큰값 = 토큰값_추출(토큰_발급_요청(EMAIL, PASSWORD));
        // when
        ExtractableResponse<Response> 최단_경로_조회 = 최단_경로_조회_요청(토큰값, 왕십리, DDP);
        // then
        최단_경로_조회_성공(최단_경로_조회, Amount.from(1650));
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * And 회원 등록되어 있음
     * And 토큰 발급(로그인)되어 있음
     * When 로그인(13세 이상 ~ 19세 미만) 상태로 최단 경로 조회
     * Then 최단 경로 조회 성공
     * And 운임에서 350원을 공제한 금액의 20%할인 요금 확인
     */
    @DisplayName("로그인(13세 이상 ~ 19세 미만) 상태로 최단 경로 조회")
    @Test
    void twenty_discount() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 15);
        String 토큰값 = 토큰값_추출(토큰_발급_요청(EMAIL, PASSWORD));
        // when
        ExtractableResponse<Response> 최단_경로_조회 = 최단_경로_조회_요청(토큰값, 왕십리, DDP);
        // then
        최단_경로_조회_성공(최단_경로_조회, Amount.from(1040));
    }

    /**
     * Given 지하철 역과 노선과 구간이 등록되어 있음
     * And 회원 등록되어 있음
     * And 토큰 발급(로그인)되어 있음
     * When 로그인(13세 미만) 상태로 최단 경로 조회
     * Then 최단 경로 조회 성공
     * And 운임에서 350원을 공제한 금액의 50%할인 요금 확인
     */
    @DisplayName("로그인(13세 미만) 상태로 최단 경로 조회")
    @Test
    void fifty_discount() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 12);
        String 토큰값 = 토큰값_추출(토큰_발급_요청(EMAIL, PASSWORD));
        // when
        ExtractableResponse<Response> 최단_경로_조회 = 최단_경로_조회_요청(토큰값, 왕십리, DDP);
        // then
        최단_경로_조회_성공(최단_경로_조회, Amount.from(650));
    }

    private void 지하철_노선과_구역_등록되어_있음_2호선() {
        Map<String, String> params = 추가요금이_있는_지하철_노선_생성_요청_파라미터(
            "2호선",
            "bg-green-600",
            왕십리,
            DDP,
            Distance.from(20),
            Amount.from(200L)
        );
        LineId LINE_2 = 지하철_노선_ID_추출(지하철_노선_등록되어_있음(params));
        지하철_노선에_지하철역_등록_요청(LINE_2, 왕십리, 신당, 10);
    }

    private void 최단_경로_조회_성공(ExtractableResponse<Response> response, Amount expectedAmount) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getInt("amount")).isEqualTo(expectedAmount.value())
        );
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(String accessToken, StationId source, StationId target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.getString());
        params.put("target", target.getString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParams(params)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }
}
