package nextstep.subway.favorite.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DisplayName("DB에 잘 저장이 되는지 확인")
class FavoriteRepositoryTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    void save() {
        // line이 입력되면 디비에 저장된다.
        // 응답으로 저장된 line이 리턴된다.

        //when
        Favorite favorite = favoriteRepository.save(new Favorite());

        // then
        assertThat(favorite).isNotNull();
        assertThat(favorite.getId()).isNotNull();
    }

    @Test
    void find() {
        //when
        favoriteRepository.save(new Favorite());
        Favorite favorite = favoriteRepository.findById(1L).orElseThrow(IllegalArgumentException::new);

        //then
        assertThat(favorite).isNotNull();
        assertThat(favorite.getId()).isEqualTo(1L);
    }
}