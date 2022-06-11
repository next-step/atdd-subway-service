package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_성공후_토큰_조회됨;
import static nextstep.subway.auth.acceptance.AuthAcceptanceSupport.로그인_시도함;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.중복으로_인해_즐겨찾기_등록_실패됨;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_등록_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceSupport.즐겨찾기_등록됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceSupport.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

    }

    // 즐겨찾기 생성
    @DisplayName("지하철역을 즐겨찾기로 등록한다")
    @Test
    void registerFavorite() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, 대림역.getId(), 구로디지털단지역.getId());

        즐겨찾기_등록됨(response);
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

    // 즐겨찾기 목록 조회

    // 즐겨찾기 삭제
}