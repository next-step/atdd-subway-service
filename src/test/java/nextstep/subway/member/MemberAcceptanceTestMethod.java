package nextstep.subway.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;

public class MemberAcceptanceTestMethod extends AcceptanceTest {
    private static final String LOCATION_HEADER_KEY = "Location";
    private static final String MEMBER_PATH = "/members";
    private static final String MEMBER_ME_PATH = "/members/me";

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
        MemberRequest memberRequest = MemberRequest.of(email, password, age);
        return post(MEMBER_PATH, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_등록됨(MemberRequest memberRequest) {
        return post(MEMBER_PATH, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_KEY);
        return get(uri);
    }

    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email, String password, Integer age) {
        String uri = response.header(LOCATION_HEADER_KEY);
        MemberRequest memberRequest = MemberRequest.of(email, password, age);
        return put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_KEY);
        return delete(uri);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_요청(TokenResponse tokenResponse) {
        return getWithAuth(MEMBER_ME_PATH, tokenResponse);
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_수정_요청(TokenResponse tokenResponse, MemberRequest memberRequest) {
        return putWithAuth(MEMBER_ME_PATH, tokenResponse, memberRequest);
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_삭제_요청(TokenResponse tokenResponse) {
        return deleteWithAuth(MEMBER_ME_PATH, tokenResponse);
    }
}
