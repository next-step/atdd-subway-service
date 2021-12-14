package nextstep.subway.favorite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        LineAcceptanceTestHelper.지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 7)
            .as(LineResponse.class);
        LineAcceptanceTestHelper.지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 2)
            .as(LineResponse.class);
        LineAcceptanceTestHelper.지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 8)
            .as(LineResponse.class);

        MemberAcceptanceTestHelper.회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = AuthAcceptanceTestHelper.로그인_되어_있음(EMAIL, PASSWORD);
        token = AuthAcceptanceTestHelper.토큰으로_변환(response);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorites() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(강남역.getId()));
        params.put("target", String.valueOf(양재역.getId()));

        // when
        ExtractableResponse<Response> createResponse1 = FavoriteAcceptanceTestHelper.즐겨찾기_생성_요청(token, params);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_생성됨(createResponse1);

        //given
        params = new HashMap<>();
        params.put("source", String.valueOf(양재역.getId()));
        params.put("target", String.valueOf(교대역.getId()));

        // when
        ExtractableResponse<Response> createResponse2 = FavoriteAcceptanceTestHelper.즐겨찾기_생성_요청(token, params);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_생성됨(createResponse2);

        // given
        Long createdId = Long.parseLong(createResponse1.header("Location").split("/")[2]);
        Long createdId2 = Long.parseLong(createResponse2.header("Location").split("/")[2]);
        List<Long> expectedIds = Arrays.asList(createdId, createdId2);

        // when
        ExtractableResponse<Response> getResponse = FavoriteAcceptanceTestHelper.즐겨찾기_목록_조회_요청(token);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_목록_조회됨(getResponse);
        FavoriteAcceptanceTestHelper.즐겨찾기_목록_예상된_결과_조회됨(getResponse, expectedIds);

        // when
        ExtractableResponse<Response> deleteResponse = FavoriteAcceptanceTestHelper.즐겨찾기_삭제_요청(token, createdId);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_삭제됨(deleteResponse);

        // given
        expectedIds = Arrays.asList(createdId2);

        // when
        getResponse = FavoriteAcceptanceTestHelper.즐겨찾기_목록_조회_요청(token);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_목록_조회됨(getResponse);
        FavoriteAcceptanceTestHelper.즐겨찾기_목록_예상된_결과_조회됨(getResponse, expectedIds);
    }

    @DisplayName("중복된 즐겨찾기 생성 시 실패한다.")
    @Test
    void duplicateCreateFavoriteError() {
        //given
        FavoriteAcceptanceTestHelper.즐겨찾기_생성되어_있음(token, 강남역, 양재역);

        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(강남역.getId()));
        params.put("target", String.valueOf(양재역.getId()));

        // when
        ExtractableResponse<Response> createResponse = FavoriteAcceptanceTestHelper.즐겨찾기_생성_요청(token, params);

        // then
        FavoriteAcceptanceTestHelper.즐겨찾기_생성_실패됨(createResponse);
    }
}