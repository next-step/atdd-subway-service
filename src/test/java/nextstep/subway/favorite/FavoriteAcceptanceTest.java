package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공후_토큰_조회됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_시도함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.유효하지_않은_토큰임;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.중복으로_인해_즐겨찾기_등록_실패됨;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_등록_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_등록됨;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_조회_검증됨;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_조회_요청;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 대림역;
    private StationResponse 구로디지털단지역;
    private StationResponse 신대방역;
    private ExtractableResponse<Response> createResponse;
    private String accessToken;
    private String 유효하지_않은_토큰;

    private ExtractableResponse<Response> 즐겨찾기;
    private ExtractableResponse<Response> 즐겨찾기_2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);
        ExtractableResponse<Response> loginSuccessResponse = 로그인_시도함(EMAIL, PASSWORD);
        accessToken = 로그인_성공후_토큰_조회됨(loginSuccessResponse);

        대림역 = StationAcceptanceTest.지하철역_등록되어_있음("대림").as(StationResponse.class);
        구로디지털단지역 = StationAcceptanceTest.지하철역_등록되어_있음("구로디지털단지").as(StationResponse.class);
        신대방역 = StationAcceptanceTest.지하철역_등록되어_있음("신대방").as(StationResponse.class);

        유효하지_않은_토큰 = "invalid token...";
    }

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     * */
    @DisplayName("지하철역을 즐겨찾기로 등록한다")
    @TestFactory
    Stream<DynamicTest> registerFavorite() {
        return Stream.of(
            dynamicTest("즐겨찾기 생성을 요청하면 즐겨찾기가 생성된다", () -> {
                즐겨찾기 = 즐겨찾기_등록_요청(accessToken, 대림역.getId(), 구로디지털단지역.getId());
                즐겨찾기_등록됨(즐겨찾기);

                즐겨찾기_2 = 즐겨찾기_등록_요청(accessToken, 대림역.getId(), 신대방역.getId());
                즐겨찾기_등록됨(즐겨찾기_2);
            }),
            dynamicTest("생성된 즐겨찾기를 조회하면 즐겨찾기 목록이 조회된다", () -> {
                ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
                즐겨찾기_조회_검증됨(response, Arrays.asList(즐겨찾기, 즐겨찾기_2));
            }),
            dynamicTest("즐겨찾기를 삭제하면 정상적으로 삭제되어야 한다 ", () -> {
                ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기);
                즐겨찾기_삭제됨(response);
            }),
            dynamicTest("삭제 후 즐겨찾기를 다시 조회하면 정상적으로 조회된다", () -> {
                ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
                즐겨찾기_조회_검증됨(response, Collections.singletonList(즐겨찾기_2));
            })
        );
    }

    @DisplayName("잘못된 토큰으로 즐겨찾기 등록을 요청하면 실패한다")
    @Test
    void registerFavorite_token_failed() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(유효하지_않은_토큰, 대림역.getId(), 구로디지털단지역.getId());
        유효하지_않은_토큰임(response);
    }

    @DisplayName("이미 등록된 지하철역들을 중복으로 등록하면 실패한다")
    @TestFactory
    Stream<DynamicTest> registerFavorite_failed() {
        return Stream.of(
            dynamicTest("지하철역을 즐겨찾기로 등록하면 등록에 성공한다" , () -> {
                ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 대림역.getId(), 구로디지털단지역.getId());
                즐겨찾기_등록됨(response);
            }),
            dynamicTest("동일한 지하철역을 즐겨찾기로 등록하면 중복되어 등록에 실패한다", () -> {
                ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 대림역.getId(), 구로디지털단지역.getId());
                중복으로_인해_즐겨찾기_등록_실패됨(response);
            })
        );
    }
}