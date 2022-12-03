package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청_후_토큰_가져오기;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MyInfoAcceptanceTest extends AcceptanceTest {

    /*
    Feature: 내 정보 관리 기능
        Background:
            Given: 회원이 가입되어 있음
            AND : 회원 계정으로 로그인_후_토큰가져오기
        Scenario:
            WHEN: 내 정보 조회
            THEN: 내 정보가 조회됨
            WHEN: 내 정보 수정
            THEN: 내 정보 정보가 수정됨
            WHEN: 내 정보를 삭제함
            THEN: 내 정보가 삭제됨
            WHEN: 내 정보를 조회
            THEN: 내 정보가 조회되지 않음
     */
    @DisplayName("내 정보 관리 기능")
    @Test
    void myInfoScenarioTest() {
        //given
        회원_생성을_요청("test@test.com", "1234", 10);
        String token = 로그인_요청_후_토큰_가져오기("test@test.com", "1234");

        //when
        MemberResponse getMemberResponse = 내_정보_조회(token).as(MemberResponse.class);
        //then
        내_정보가_조회됨(getMemberResponse);

        //when
        MemberResponse putMemberResponse = 내정보_수정(token).as(MemberResponse.class);
        //then
        내_정보가_수정됨(putMemberResponse);

        //when
        ExtractableResponse<Response> deleteResponse = 내정보_삭제(token);
        //then
        내정보_삭제됨(deleteResponse);

        //when
        ExtractableResponse<Response> getResponse = 내_정보_조회(token);
        //then
        내_정보_조회되지_않음(getResponse);
    }


}
