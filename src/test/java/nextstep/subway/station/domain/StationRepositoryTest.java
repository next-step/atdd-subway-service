package nextstep.subway.station.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("exist 학습 테스트")
    @Test
    void isExist() {
        boolean result = stationRepository.existsById(1L);
        assertThat(result).isFalse();

        Station saved = stationRepository.save(new Station("hi"));
        entityManager.flush();

        boolean result2 = stationRepository.existsById(saved.getId());
        assertThat(result2).isTrue();
    }
}