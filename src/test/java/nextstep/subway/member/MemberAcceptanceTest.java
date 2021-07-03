package nextstep.subway.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static nextstep.subway.PageController.URIMapping.MEMBERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("회원 정보를 관리한다.")
public class MemberAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(MEMBERS);

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PASSWORD = "newpassword";
    public static final int AGE = 20;
    public static final int NEW_AGE = 21;

    private ExtractableResponse<Response> createResponse;

    @BeforeEach
    public void setup() {
        createResponse = requestCreateMember(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void createMember() {
        //given
        회원_생성됨(createResponse);

        // when
        long 회원_ID = RestAssuredTemplate.getLocationId(createResponse);
        ExtractableResponse<Response> findResponse = requestFindMember(회원_ID);

        // then
        MemberResponse memberResponse = findResponse.as(MemberResponse.class);
        assertAll(
                () -> assertThat(memberResponse.getId()).isNotNull(),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(AGE)
        );
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void manageMember() {
        //given
        회원_생성됨(createResponse);

        // when
        long 회원_ID = RestAssuredTemplate.getLocationId(createResponse);
        ExtractableResponse<Response> updateResponse = requestUpdateMemberInfo(회원_ID, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        //given
        회원_생성됨(createResponse);

        // when
        long 회원_ID = RestAssuredTemplate.getLocationId(createResponse);
        ExtractableResponse<Response> deleteResponse = requestDeleteMember(회원_ID);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#createMember
     */
    public static ExtractableResponse<Response> requestCreateMember(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.post(memberRequest);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#findMember
     */
    public static ExtractableResponse<Response> requestFindMember(Long id) {
        return restAssuredTemplate.get(id);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#updateMember
     */
    public static ExtractableResponse<Response> requestUpdateMemberInfo(Long id, String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        return restAssuredTemplate.put(id, memberRequest);
    }

    /**
     * @see nextstep.subway.member.ui.MemberController#deleteMember
     */
    public static ExtractableResponse<Response> requestDeleteMember(Long id) {
        return restAssuredTemplate.delete(id);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
