package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static nextstep.subway.PageController.URIMapping.MEMBERS;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate  = new RestAssuredTemplate(MEMBERS);

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
        ExtractableResponse<Response> createResponse = requestCreateMember(EMAIL, PASSWORD, AGE);
        long 회원_ID = RestAssuredTemplate.getLocationId(createResponse);

        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = requestFindMember(회원_ID);
        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // when
        ExtractableResponse<Response> updateResponse = requestUpdateMemberInfo(회원_ID, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        // then
        회원_정보_수정됨(updateResponse);

        // when
        ExtractableResponse<Response> deleteResponse = requestDeleteMember(회원_ID);
        // then
        회원_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {

    }

    public static ExtractableResponse<Response> requestCreateMember(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.post(memberRequest);
    }

    public static ExtractableResponse<Response> requestFindMember(Long id) {
        return restAssuredTemplate.get(id);
    }

    public static ExtractableResponse<Response> requestUpdateMemberInfo(Long id, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.put(id, memberRequest);
    }

    public static ExtractableResponse<Response> requestDeleteMember(Long id) {
        return restAssuredTemplate.delete(id);
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
}
