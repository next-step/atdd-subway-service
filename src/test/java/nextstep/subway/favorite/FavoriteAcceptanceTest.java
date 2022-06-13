package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTestMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestMethod.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인_성공_이후_토큰;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */

    private StationResponse 가락시장역;
    private StationResponse 경찰병원역;
    private StationResponse 오금역;

    private String 로그인_토큰;

    @BeforeEach
    void before() {
        //given: 지하철역 등록되어 있음
        가락시장역 = StationAcceptanceTest.지하철역_등록되어_있음("가락시장역").as(StationResponse.class);
        경찰병원역 = StationAcceptanceTest.지하철역_등록되어_있음("경찰병원역").as(StationResponse.class);
        오금역 = StationAcceptanceTest.지하철역_등록되어_있음("오금역").as(StationResponse.class);

        // 3호선 등록 지하철역 : 가락시장역 - 경찰병원역 - 오금역
        LineRequest lineRequest = new LineRequest("삼호선", "bg-orange-600", 가락시장역.getId(), 경찰병원역.getId(), 10);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 경찰병원역, 오금역, 3);

        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 30;
        // 회원 등록
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // 로그인됨
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_토큰 = 로그인_성공_이후_토큰(로그인_요청_응답);
    }


}
