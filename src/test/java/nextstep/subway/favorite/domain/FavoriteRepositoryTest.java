package nextstep.subway.favorite.domain;

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

@DataJpaTest
public class FavoriteRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    Member saveMember;
    Station source;
    Station target;

    Favorite saveFavorite;

    @BeforeEach
    void setUp() {
        saveMember = memberRepository.save(new Member("test@email.com","1234", 1));
        source = stationRepository.save(new Station("강남역"));
        target = stationRepository.save(new Station("삼성역"));

        saveFavorite = favoriteRepository.save(new Favorite(saveMember, source, target));
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void save() {
        assertThat(saveFavorite).isNotNull();
        assertThat(saveFavorite.getId()).isNotNull();
    }

    @DisplayName("계정의 즐겨찾기를 불러온다.")
    @Test
    void findByMember() {
        List<Favorite> favorites = favoriteRepository.findByMember(saveMember);

        assertThat(favorites).contains(saveFavorite);
    }

    @DisplayName("계정의 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteById() {
        favoriteRepository.deleteByIdAndMember(saveFavorite.getId(), saveFavorite);

        List<Favorite> favorites = favoriteRepository.findByMember(saveMember);

        assertThat(favorites.size()).isEqualTo(0);
    }
}
