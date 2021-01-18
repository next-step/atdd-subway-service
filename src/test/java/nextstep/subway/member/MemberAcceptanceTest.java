package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestSupport.*;
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
        // Given
        // 회원 등록되어 있음
        String accessToken = 로그인_되어_있음();

        // When
        // 토큰으로 내 정보 요청
        ExtractableResponse<Response> myInfoResponse = 나의_정보_요청(accessToken);
        MemberResponse memberResponse = myInfoResponse.as(MemberResponse.class);

        // Then
        // 내 정보 조회 됨
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);

        // When
        // 회원_정보_수정_요청
        ExtractableResponse<Response> myInfoUpdateResponse
                = 나의_정보_수정_요청(accessToken, new MemberRequest(EMAIL, PASSWORD, NEW_AGE));
        MemberResponse memberResponseAfterUpdate = 나의_정보_요청(accessToken).as(MemberResponse.class);

        // Then
        // 회원_정보_수정됨
        assertThat(myInfoUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponseAfterUpdate.getAge()).isEqualTo(memberResponse.getAge());

        // When
        // 회원_삭제_요청
        ExtractableResponse<Response> myInfoDeleteResponse = 나의_정보_삭제_요청(accessToken);

        ExtractableResponse<Response> deletedMyInfoResponse = 나의_정보_요청(accessToken);

        // Then
        // 회원_삭제됨
        assertThat(myInfoDeleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(deletedMyInfoResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
