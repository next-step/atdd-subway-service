package nextstep.subway.favorite.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class FavoriteRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @DisplayName("unique 제약 조건 확인")
    @Test
    void uniqueTest() {
        favoriteRepository.save(new Favorite(1L, 2L, 3L));
        entityManager.flush();

        assertThatThrownBy(() -> favoriteRepository.save(new Favorite(1L, 2L, 3L)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}