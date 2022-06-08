package nextstep.subway;

import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class AcceptanceTest extends RestAssuredTest{
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        super.setUp();
        databaseCleanup.execute();
    }
}
