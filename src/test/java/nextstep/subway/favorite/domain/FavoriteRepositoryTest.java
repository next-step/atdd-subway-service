package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;


    @DisplayName("즐겨찾기 생성 검증")
    @Test
    void createFavorite() {
        Member 사용자 = memberRepository.save(new Member("test@email.com", "password", 20));
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 건대역 = stationRepository.save(new Station("건대역"));

        Favorite 즐겨찾기 = favoriteRepository.save(new Favorite(사용자, 강남역, 건대역));

        assertThat(즐겨찾기).isNotNull();
    }

    @DisplayName("목록 표출 검증")
    @Test
    void list() {
        Member 사용자 = memberRepository.save(new Member("test@email.com", "password", 20));
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 건대역 = stationRepository.save(new Station("건대역"));
        Station 사당역 = stationRepository.save(new Station("사당역"));
        Favorite 즐겨찾기1 = favoriteRepository.save(new Favorite(사용자, 강남역, 건대역));
        Favorite 즐겨찾기2 = favoriteRepository.save(new Favorite(사용자, 강남역, 사당역));

        List<Favorite> favorites = favoriteRepository.findByMemberId(사용자.getId());

        assertThat(favorites).isEqualTo(Arrays.asList(즐겨찾기1, 즐겨찾기2));

    }
}
