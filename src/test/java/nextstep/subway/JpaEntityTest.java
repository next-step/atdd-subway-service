package nextstep.subway;

import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DatabaseCleanup.class)
public class JpaEntityTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
    }

    protected void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
