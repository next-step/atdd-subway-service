package nextstep.subway.favorite.domain;

import nextstep.subway.RepositoryTest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FavoriteRepository 클래스 테스트")
@DataJpaTest
public class FavoriteRepositoryTest extends RepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Station 강남역;
    private Station 역삼역;
    private Member 사용자;

    @BeforeEach
    public void setUp(@Autowired StationRepository stationRepository, @Autowired MemberRepository memberRepository) {
        super.setUp();
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));

        사용자 = memberRepository.save(new Member("heowc1992@gmail.com", "password", 31));
    }

    @DisplayName("Favorite를 저장한다.")
    @Test
    void save() {
        Favorite favorite = new Favorite(사용자, 강남역, 역삼역);

        Favorite createdFavorite = favoriteRepository.save(favorite);

        assertThat(createdFavorite).isNotNull();
        assertThat(createdFavorite.getMember()).isEqualTo(사용자);
        assertThat(createdFavorite.getSource()).isEqualTo(강남역);
        assertThat(createdFavorite.getTarget()).isEqualTo(역삼역);
    }

    @DisplayName("Favorite 목록을 조회한다.")
    @Test
    void findAll() {
        favoriteRepository.save(new Favorite(사용자, 강남역, 역삼역));

        List<Favorite> favorites = favoriteRepository.findAll();

        assertThat(favorites).hasSize(1);
    }

    @DisplayName("Favorite를 제거한다.")
    @Test
    void delete() {
        Favorite favorite = favoriteRepository.save(new Favorite(사용자, 강남역, 역삼역));

        favoriteRepository.delete(favorite);

        List<Favorite> favorites = favoriteRepository.findAll();
        assertThat(favorites).hasSize(0);
    }
}
