package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceStep.로그인된_회원;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.*;
import static nextstep.subway.line.acceptance.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceStep.생성된_다른_회원;
import static nextstep.subway.member.MemberAcceptanceStep.생성된_회원;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "koreatech93@naver.com";
    private static final String PASSWORD = "123123";
    private static final int AGE = 30;

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private TokenResponse myToken;

    @BeforeEach
    public void favoriteSetup() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMyInfo() {
        생성된_회원(EMAIL, PASSWORD, AGE);
        myToken = 로그인된_회원(EMAIL, PASSWORD).as(TokenResponse.class);
        return Stream.of(
                DynamicTest.dynamicTest("내 즐겨찾기 등록", () -> {
                    // given
                    FavoriteRequest request = new FavoriteRequest(강남역.getId(), 양재역.getId());

                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_등록_요청(myToken, request);

                    // then
                    즐겨찾기_등록됨(response);
                }),
                DynamicTest.dynamicTest("내 즐겨찾기 조회", () -> {
                    // given
                    등록된_즐겨찾기(myToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));
                    등록된_즐겨찾기(myToken, new FavoriteRequest(양재역.getId(), 정자역.getId()));

                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(myToken);

                    // then
                    즐겨찾기_목록_조회됨(response);
                }),
                DynamicTest.dynamicTest("내 즐겨찾기 삭제", () -> {
                    // given
                    ExtractableResponse<Response> favoriteResponse = 등록된_즐겨찾기(myToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));

                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(myToken, favoriteResponse);

                    // then
                    즐겨찾기_삭제됨(response);
                }),
                DynamicTest.dynamicTest("다른회원의 즐겨찾기 삭제", () -> {
                    // given
                    String otherEmail = "xxx@naver.com";
                    생성된_다른_회원(otherEmail, PASSWORD, AGE);
                    TokenResponse otherToken = 로그인_요청(otherEmail, PASSWORD).as(TokenResponse.class);
                    ExtractableResponse<Response> favoriteResponse = 등록된_즐겨찾기(otherToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));


                    // when
                    ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(myToken, favoriteResponse);

                    // then
                    즐겨찾기_삭제_실패(response);
                })
        );
    }
}
