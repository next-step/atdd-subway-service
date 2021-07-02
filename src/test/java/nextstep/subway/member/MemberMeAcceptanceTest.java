package nextstep.subway.member;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.utils.RestAssuredTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.PageController.URIMapping.MEMBERS;

@DisplayName("나의 정보를 관리한다.")
public class MemberMeAcceptanceTest extends AcceptanceTest {
    public static final RestAssuredTemplate restAssuredTemplate = new RestAssuredTemplate(MEMBERS + "/me");

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void findMyInfo() {

    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMyInfo() {

    }

    @DisplayName("나의 정보를 삭제한다.")
    @Test
    void deleteMyInfo() {

    }

}
