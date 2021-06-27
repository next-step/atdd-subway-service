package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setBeforeEach(){
        //회원 등록됨
    }

    @DisplayName("올바른 이메일과 비밀번호로 로그인한다")
    @Test
    void 올바른_이메일_비밀번호로_로그인() {
        //로그인_요청
        //로그인_성공
    }

    @DisplayName("등록되지 않은 이메일로 로그인한다")
    @Test
    void 등록되지_않은_이메일로_로그인_시도() {
        //로그인_요청
        //로그인_실패
    }

    @DisplayName("잘못된 비밀번호로 로그인한다")
    @Test
    void 잘못된_비밀번호로_로그인_시도() {
        //로그인_요청
        //로그인_실패
    }

    @DisplayName("유효하지 않은 토큰으로 내 정보를 조회한다")
    @Test
    void 유효하지_않은_토큰으로_내정보_조회() {
        //내정보_조회_요청
        //조회_실패
    }

    @DisplayName("유효하지 않은 토큰으로 즐겨찾기를 조회한다")
    @Test
    void 유효하지_않은_토큰으로_즐겨찾기_조회() {
        //즐겨찾기_조회_요청
        //조회_실패
    }
}
