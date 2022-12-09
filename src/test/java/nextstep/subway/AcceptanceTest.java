package nextstep.subway;

import io.restassured.RestAssured;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_되어_있음;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    public static String children;
    public static String teenager;
    public static String memberA;
    @LocalServerPort
    int port;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        회원_생성_되어_있음(EMAIL, PASSWORD, AGE);
        회원_생성_되어_있음(TEENAGER_EMAIL, TEENAGER_PASSWORD, TEENAGER_AGE);
        회원_생성_되어_있음(CHILDREN_EMAIL, CHILDREN_PASSWORD, CHILDREN_AGE);
        memberA = 로그인_되어_있음(EMAIL, PASSWORD);
        teenager = 로그인_되어_있음(TEENAGER_EMAIL, TEENAGER_PASSWORD);
        children = 로그인_되어_있음(CHILDREN_EMAIL, CHILDREN_PASSWORD);
    }
}
