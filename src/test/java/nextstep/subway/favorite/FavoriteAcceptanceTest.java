package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private static String token;

    private LineResponse 일호선;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private StationResponse 화곡역;
    private StationResponse 선릉역;
    private StationResponse 영등포역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given 지하철 노선 / 역 등록되어 있음
        강남역 = 지하철역_생성_요청("강남역");
        교대역 = 지하철역_생성_요청("교대역");
        잠실역 = 지하철역_생성_요청("잠실역");
        삼성역 = 지하철역_생성_요청("삼성역");
        화곡역 = 지하철역_생성_요청("화곡역");
        선릉역 = 지하철역_생성_요청("선릉역");
        영등포역 = 지하철역_생성_요청("영등포역");

        일호선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("1호선", "green", 1L, 3L, 10)).as(LineResponse.class);
        LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("2호선", "puple", 5L, 6L, 10));

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 잠실역, 교대역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 교대역, 삼성역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 삼성역, 선릉역, 10);

        // and
        // 회원이 생성 되어 있음
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // and
        // 토큰을 발급 받음
        ExtractableResponse<Response> responseForToken = MemberAcceptanceTest.토큰_발급을_요청(EMAIL, PASSWORD);
        token = responseForToken.jsonPath().getString("accessToken");
    }

    @DisplayName("즐겨 찾기 기능 테스트")
    @Test
    void 즐겨찾기_기능_테스트() {
        // 내 정보 등록되어 있음
        MemberResponse myInfo = MemberAcceptanceTest.나의_정보를_요청(token).as(MemberResponse.class);
        assertThat(myInfo.getEmail()).isEqualTo(EMAIL);

        // when
        ExtractableResponse<Response> responseForFavorite = 즐겨찾기_생성을_요청(token, 3L, 4L);
        // then
        즐겨찾기_생성됨(responseForFavorite);

        // when
        ExtractableResponse<Response> responseForFavoriteList = 즐겨찾기_목록을_요청(token);
        // then
        즐겨찾기_목록_조회됨(responseForFavoriteList);

        // when
        ExtractableResponse<Response> responseForDeleteFavorite = 즐겨찾기_삭제를_요청(token, 1L);
        // then
        즐겨찾기_삭제됨(responseForDeleteFavorite);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> responseForDeleteFavorite) {
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제를_요청(String token, long l) {
        return null;
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> responseForFavoriteList) {

    }

    private ExtractableResponse<Response> 즐겨찾기_목록을_요청(String token) {
        return null;
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> responseForFavorite) {
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, long l, long l1) {
        return null;
    }

    private StationResponse 지하철역_생성_요청(String name) {
        return StationAcceptanceTest.지하철역_생성_요청(name).as(StationResponse.class);
    }

}