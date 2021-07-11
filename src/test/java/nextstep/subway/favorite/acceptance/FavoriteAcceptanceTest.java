package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.AuthToken;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.IdTransferObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.domain.AuthTestSnippet.로그인_요청_및_성공_확인;
import static nextstep.subway.favorite.domain.FavoriteTestSnippet.*;
import static nextstep.subway.line.domain.LineSectorTestSnippet.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.domain.LineTestSnippet.지하철_노선_생성_요청;
import static nextstep.subway.member.domain.MemberTestSnippet.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static nextstep.subway.station.dto.StationResponse.of;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private String email1 = "jordy-torvalds@jordy.com";
    private String password1 = "jordy";
    private int age1 = 29;

    private String email2 = "scappy@scappy.com";
    private String password2 = "scappy";
    private int age2 = 29;

    private Station 양평역;
    private Station 영등포구청역;
    private Station 영등포시장역;
    private Station 모란역;
    private Station 야탑역;
    private AuthToken authToken = new AuthToken();

    private IdTransferObject ido = new IdTransferObject();

    @BeforeEach
    public void setUp() {
        super.setUp();

        양평역 = 지하철역_생성_요청("양평역").as(Station.class);
        영등포구청역 = 지하철역_생성_요청("영등포구청역").as(Station.class);
        영등포시장역 = 지하철역_생성_요청("영등포시장역").as(Station.class);
        모란역 = 지하철역_생성_요청("모란역").as(Station.class);
        야탑역 = 지하철역_생성_요청("야탑역").as(Station.class);
        LineResponse line5Response
                = 지하철_노선_생성_요청(new LineRequest("5호선", "보라색", 양평역.getId(), 영등포구청역.getId(), 10))
                .as(LineResponse.class);
        LineResponse lineNewBundangResponse
                = 지하철_노선_생성_요청(new LineRequest("신분당선", "노란색", 모란역.getId(), 야탑역.getId(), 10))
                .as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(line5Response, of(영등포구청역), of(영등포시장역), 30);

        회원_생성을_요청(email1, password1, age1);
        회원_생성을_요청(email2, password2, age2);
    }

    @DisplayName("즐겨찾기를 관리 한다")
    @TestFactory
    Stream<DynamicTest> manageFavorite() {
        return Stream.of(
                dynamicTest("로그인_요청_및_성공_확인", 로그인_요청_및_성공_확인(email1, password1, authToken)),
                dynamicTest("즐겨찾기_생성_및_성공_확인(양평역 - 영등포구청역)", 즐겨찾기_생성_및_성공_확인(authToken, 양평역, 영등포구청역, ido)),
                dynamicTest("유효하지_않은_즐겨찾기_생성_및_실패_확인(양평역 - 야탑역)", 유효하지_않은_즐겨찾기_생성_및_실패_확인(authToken, 양평역, 야탑역, ido)),
                dynamicTest("즐겨찾기_조회_및_성공_확인(양평역 - 영등포구청역)", 즐겨찾기_조회_및_성공_확인(authToken, 양평역, 영등포구청역)),
                dynamicTest("즐겨찾기_삭제_및_성공_확인(양평역 - 영등포구청역)", 즐겨찾기_삭제_및_성공_확인(authToken, ido)),
                dynamicTest("즐겨찾기_생성_및_성공_확인(양평역 - 영등포구청역)", 즐겨찾기_생성_및_성공_확인(authToken, 영등포구청역, 영등포시장역, ido)),
                dynamicTest("로그인_요청_및_성공_확인", 로그인_요청_및_성공_확인(email2, password2, authToken)),
                dynamicTest("타 회원 즐겨찾기 삭제 및 실패 확인(양평역 - 영등포역)", 타_회원_즐겨찾기_삭제_및_실패_확인(authToken, ido))
        );
    }


}