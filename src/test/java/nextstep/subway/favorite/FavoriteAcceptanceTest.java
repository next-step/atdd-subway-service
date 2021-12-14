package nextstep.subway.favorite;

import static nextstep.subway.favorite.FavoriteAcceptanceTestHelper.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTestHelper;
import nextstep.subway.line.acceptance.LineAcceptanceTestHelper;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTestHelper;
import nextstep.subway.station.StationAcceptanceTestHelper;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("강남역")
            .as(StationResponse.class);
        양재역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("양재역")
            .as(StationResponse.class);
        교대역 = StationAcceptanceTestHelper.지하철역_등록되어_있음("교대역")
            .as(StationResponse.class);

        신분당선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 7)
            .as(LineResponse.class);
        이호선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 2)
            .as(LineResponse.class);
        삼호선 = LineAcceptanceTestHelper.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 8)
            .as(LineResponse.class);

        MemberAcceptanceTestHelper.회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = AuthAcceptanceTestHelper.로그인_되어_있음(EMAIL, PASSWORD);
        token = AuthAcceptanceTestHelper.토큰으로_변환(response);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(강남역.getId()));
        params.put("target", String.valueOf(양재역.getId()));
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, params);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_생성됨(response);
    }
}