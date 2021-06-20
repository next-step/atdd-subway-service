package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.AuthHelper.로그인_요청;
import static nextstep.subway.member.MemberHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        //when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        //given
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(tokenRequest);
        String accessToken = loginResponse.jsonPath().getObject(".", TokenResponse.class).getAccessToken();
        //when
        ExtractableResponse findMeResponse = 내_정보_찾기(accessToken);
        //then
        assertThat(findMeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findMeResponse.jsonPath().getObject(".", MemberResponse.class).getEmail()).isEqualTo(EMAIL);


        //given
        MemberRequest updateMemberRequest = new MemberRequest(EMAIL, PASSWORD, NEW_AGE);
        //when
        ExtractableResponse updateMineResponse = 내_정보_업데이트(accessToken, updateMemberRequest);
        //then
        assertThat(updateMineResponse.jsonPath().getObject(".", MemberResponse.class).getAge()).isEqualTo(NEW_AGE);

        //given
        ExtractableResponse deleteMineResponse = 내_정보_삭제(accessToken, updateMemberRequest);
        //then
        assertThat(deleteMineResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
