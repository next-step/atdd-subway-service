package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteRepositoryTest {
    Station 강남역;
    Station 정자역;
    Member 사용자;
    Favorite 즐겨찾기;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(Station.builder("강남역")
                .build());
        정자역 = stationRepository.save(Station.builder("정자역")
                .build());
        사용자 = memberRepository.save(new Member("email@email.com", "password", 20));
        즐겨찾기 = Favorite.builder(사용자, 강남역, 정자역)
                .build();
    }

    @DisplayName("즐겨찾기 저장 테스트")
    @Test
    void save() {
        Favorite favorite = favoriteRepository.save(즐겨찾기);
        assertAll(
                () -> assertThat(favorite.id()).isNotNull(),
                () -> assertThat(favorite.source()).isEqualTo(강남역),
                () -> assertThat(favorite.target()).isEqualTo(정자역),
                () -> assertThat(favorite.member()).isEqualTo(사용자)
        );
    }

    @DisplayName("즐겨찾기 회원 ID로 조회 테스트")
    @Test
    void findByMemberId() {
        favoriteRepository.save(즐겨찾기);
        List<Favorite> favorites = favoriteRepository.findByMemberId(사용자.getId());
        assertThat(favorites).containsExactly(즐겨찾기);
    }

    @DisplayName("즐겨찾기 제거 테스트")
    @Test
    void delete() {
        Favorite favorite = favoriteRepository.save(즐겨찾기);
        favoriteRepository.deleteById(favorite.id());
        List<Favorite> favorites = favoriteRepository.findByMemberId(사용자.getId());
        assertThat(favorites).isEmpty();
    }
}
