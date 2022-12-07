package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FavoriteRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StationRepository stationRepository;

    Member saveMember;
    Station source;
    Station target;

    @BeforeEach
    void setUp() {
        saveMember = memberRepository.save(new Member("test@email.com","1234", 1));
        source = stationRepository.save(new Station("강남역"));
        target = stationRepository.save(new Station("삼성역"));
    }

    @DisplayName("즐겨찾기를 생성한다")
    void save() {
        Favorite saveFavorite = favoriteRepository.save(new Favorite(saveMember, source, target));

        assertThat(saveFavorite).isNotNull();
        assertThat(saveFavorite.getId()).isNotNull();
    }
}
