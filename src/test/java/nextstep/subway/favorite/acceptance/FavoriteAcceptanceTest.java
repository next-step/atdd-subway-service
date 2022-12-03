package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestFixture.로그인_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_등록_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_등록됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestFixture.즐겨찾기_삭제됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTestFixture.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestFixture.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestFixture.회원_생성을_요청;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collection;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 30;

    private StationResponse 마곡역;
    private StationResponse 공덕역;
    private StationResponse 종로3가역;
    private LineResponse 오호선;
    private String accessToken;

    @BeforeEach
    void setup() {
        마곡역 = 지하철역_등록되어_있음("마곡역").as(StationResponse.class);
        공덕역 = 지하철역_등록되어_있음("공덕역").as(StationResponse.class);
        종로3가역 = 지하철역_등록되어_있음("종로3가역").as(StationResponse.class);

        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 마곡역.getId(), 공덕역.getId(), 10)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(오호선, 공덕역, 종로3가역, 10);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *  Scenario: 즐겨찾기를 관리
     *      When 즐겨찾기 생성을 요청
     *      Then 즐겨찾기 생성됨
     *      When 즐겨찾기 목록 조회 요청
     *      Then 즐겨찾기 목록 조회됨
     *      When 즐겨찾기 삭제 요청
     *      Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @TestFactory
    Collection<DynamicTest> manageMyInfo() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(마곡역.getId(), 공덕역.getId());

        return Arrays.asList(
            dynamicTest("즐겨찾기를 생성한다.", () -> {
                // when
                ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(accessToken, favoriteRequest);
                // then
                즐겨찾기_등록됨(createResponse);
            }),
            dynamicTest("즐겨찾기 목록을 조회한다.", () -> {
                // when
                ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
                // then
                즐겨찾기_목록_조회됨(findResponse);
            }),
            dynamicTest("즐겨찾기를 삭제한다.", () -> {
                // when
                ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 1L);
                // then
                즐겨찾기_삭제됨(deleteResponse);
            })
        );
    }
}