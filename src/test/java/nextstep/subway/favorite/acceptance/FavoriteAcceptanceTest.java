package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.fixture.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

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
     */

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 10;
    private static final String NEWEMAIL = "newemail@email.com";
    private static final String NEWPASSWORD = "newpassword";
    private static final Integer NEWAGE = 20;
    private StationResponse 잠실역 = null;
    private StationResponse 잠실나루역 = null;
    private LineResponse 이호선 = null;
    private StationResponse 잠실새내역 = null;
    private MemberResponse 이회원 = null;
    private String 토큰 = null;
    private String 뉴토큰 = null;

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void myFavorite() {
        잠실역 = TestStationFactory.지하철_역_생성_요청("잠실역").as(StationResponse.class);
        잠실나루역 = TestStationFactory.지하철_역_생성_요청("잠실나루역").as(StationResponse.class);
        이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("2호선", "bg-red-600", 잠실역.getId()
                , 잠실나루역.getId(), 10, new BigDecimal(900))).as(LineResponse.class);
        잠실새내역 = TestStationFactory.지하철_역_생성_요청("잠실새내역").as(StationResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(이호선, 잠실새내역, 잠실역, 10);
        TestMemberFactory.회원_등록_요청(EMAIL, PASSWORD, AGE);
        토큰 = TestLoginFactory.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> createResponse = TestFavoriteFactory.즐겨찾기_생성_요청(토큰, 잠실역.getId(), 잠실나루역.getId());
        // then
        TestFavoriteFactory.즐겨찾기_생성됨(createResponse);

        // given
        FavoriteResponse 즐겨찾기 = createResponse.as(FavoriteResponse.class);
        // when
        ExtractableResponse<Response> findResponse = TestFavoriteFactory.즐겨찾기_목록_조회_요청(토큰, 즐겨찾기.getId());
        // then
        TestFavoriteFactory.즐겨찾기_목록_조회_요청됨(findResponse);

        //given
        TestMemberFactory.회원_등록_요청(NEWEMAIL, NEWPASSWORD, NEWAGE);
        뉴토큰 = TestLoginFactory.로그인_요청(NEWEMAIL, NEWPASSWORD).as(TokenResponse.class).getAccessToken();
        // when
        ExtractableResponse<Response> newTokenDeleteResponse = TestFavoriteFactory.즐겨찾기_삭제_요청(뉴토큰, 즐겨찾기.getId());
        // then
        TestFavoriteFactory.즐겨찾기_삭제_실패됨(newTokenDeleteResponse);

        // when
        ExtractableResponse<Response> deleteResponse = TestFavoriteFactory.즐겨찾기_삭제_요청(토큰, 즐겨찾기.getId());
        // then
        TestFavoriteFactory.즐겨찾기_삭제됨(deleteResponse);
    }
}