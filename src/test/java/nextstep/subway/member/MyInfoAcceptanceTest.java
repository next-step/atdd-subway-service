package nextstep.subway.member;

import static nextstep.subway.auth.acceptance.AuthFixture.로그인_요청_후_토큰_가져오기;
import static nextstep.subway.member.MemberFixture.AGE;
import static nextstep.subway.member.MemberFixture.EMAIL;
import static nextstep.subway.member.MemberFixture.NEW_AGE;
import static nextstep.subway.member.MemberFixture.NEW_EMAIL;
import static nextstep.subway.member.MemberFixture.NEW_PASSWORD;
import static nextstep.subway.member.MemberFixture.PASSWORD;
import static nextstep.subway.member.MemberFixture.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
            AND: 새로운 정보로 재 로그인
            WHEN: 내 정보를 삭제함
            THEN: 내 정보가 삭제됨
            WHEN: 내 정보를 조회
            THEN: 내 정보가 조회되지 않음
     */
    @DisplayName("내 정보 관리 기능")
    @Test
    void myInfoScenarioTest() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        String token = 로그인_요청_후_토큰_가져오기(EMAIL, PASSWORD);

        //when
        MemberResponse getMemberResponse = MyInfoFixture.내_정보_조회(token).as(MemberResponse.class);
        //then
        내_정보가_조회됨(getMemberResponse);

        //when
        MyInfoFixture.내정보_수정(token, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        token = 로그인_요청_후_토큰_가져오기(NEW_EMAIL, NEW_PASSWORD);
        MemberResponse putMemberResponse = MyInfoFixture.내_정보_조회(token).as(MemberResponse.class);
        //then
        내_정보가_수정됨(putMemberResponse);

        //when
        ExtractableResponse<Response> deleteResponse = MyInfoFixture.내정보_삭제(token);
        //then
        내정보_삭제됨(deleteResponse);

        //when
        ExtractableResponse<Response> getResponse = MyInfoFixture.내_정보_조회(token);
        //then
        내_정보_조회되지_않음(getResponse);
    }

    private void 내_정보가_조회됨(final MemberResponse memberResponse) {
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    private void 내_정보가_수정됨(final MemberResponse memberResponse) {
        assertThat(memberResponse.getEmail()).isEqualTo(NEW_EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(NEW_AGE);
    }

    private void 내정보_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 내_정보_조회되지_않음(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
