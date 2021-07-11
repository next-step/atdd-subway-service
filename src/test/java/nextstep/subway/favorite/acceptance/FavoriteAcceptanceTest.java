package nextstep.subway.favorite.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.AuthToken;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.IdTransferObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static nextstep.subway.auth.domain.AuthTestSnippet.로그인_요청_및_성공_확인;
import static nextstep.subway.favorite.domain.FavoriteTestSnippet.*;
import static nextstep.subway.member.domain.MemberTestSnippet.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String email = "jordy-torvalds@jordy.com";
    private String password = "jordy";
    private int age = 29;

    private Station 양평역;
    private Station 영등포역;
    private LoginMember 로그인_죠르디 = new LoginMember(1L, "jordy-torvalds@jordy.com", 29);
    private AuthToken authToken = new AuthToken();

    private IdTransferObject ido = new IdTransferObject();

    @BeforeEach
    public void setUp() {
        super.setUp();

        양평역 = 지하철역_생성_요청("양평역").as(Station.class);
        영등포역 = 지하철역_생성_요청("영등포역").as(Station.class);
        회원_생성을_요청(email, password, age);
    }

    @DisplayName("즐겨찾기를 관리 한다")
    @TestFactory
    Stream<DynamicTest> manageFavorite() {
        return Stream.of(
                dynamicTest("로그인_요청_및_성공_확인", 로그인_요청_및_성공_확인(email, password, authToken)),
                dynamicTest("즐겨찾기 생성(양평역 - 영등포역)", 즐겨찾기_생성_및_성공_확인(authToken, 양평역, 영등포역, ido)),
                dynamicTest("즐겨찾기 조회(양평역 - 영등포역)", 즐겨찾기_조회_및_성공_확인(authToken, 양평역, 영등포역)),
                dynamicTest("즐겨찾기 삭제(양평역 - 영등포역)", 즐겨찾기_삭제_및_성공_확인(authToken, ido))
        );
    }


}