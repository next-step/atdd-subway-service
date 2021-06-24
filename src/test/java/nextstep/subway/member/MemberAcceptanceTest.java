package nextstep.subway.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptancePerMethodTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptancePerMethodTest {

    public static final MemberRequest 회원 = new MemberRequest("jhh992000@gmail.com", "1234", 39);
    public static final MemberRequest 수정회원 = new MemberRequest("newemail@email.com", "newpassword", 21);
    public static final MemberRequest 비회원 = new MemberRequest("nonexistent@gmail.com", "1234", 20);

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(회원);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
        // then
        회원_정보_조회됨(findResponse, 회원);

        // when
        ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, 수정회원);
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
        // when
        // 로그인 요청
        // then
        // 로그인 성공 + 토큰수신

        // when
        // 나의정보 조회
        // then
        // 나의정보 조회 성공

        // when
        // 나의정보 수정
        // then
        // 나의정보 수정 성공

        // when
        // 회원탈퇴 요청
        // then
        // 회원탈퇴 성공
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(MemberRequest memberRequest) {
        return post(memberRequest, "/members");
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        return get(response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        return put(memberRequest, response.header("Location"));
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        return delete(response.header("Location"));
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, MemberRequest memberRequest) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(memberRequest.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(memberRequest.getAge());
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
