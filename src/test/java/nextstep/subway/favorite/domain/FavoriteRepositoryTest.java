package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("즐겨찾기 JPA 관련 기능 테스트")
@DataJpaTest
@Sql(scripts = "classpath:scripts/FavoriteInitData.sql")
public class FavoriteRepositoryTest {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    Station 서울역;
    Station 용산역;
    Member member;

    @BeforeEach
    void setUp() {
        서울역 = stationRepository.findById(1L).get();
        용산역 = stationRepository.findById(2L).get();

        member = memberRepository.findById(1L).get();
    }

    @Test
    @DisplayName("즐겨찾기 저장")
    void save() {

        Favorite persist = favoriteRepository.save(Favorite.of(서울역, 용산역, member));

        Optional<Favorite> find = favoriteRepository.findById(persist.getId());

        assertAll(() -> {
            assertTrue(find.isPresent());
            assertThat(find.get().getSource()).isEqualTo(서울역);
            assertThat(find.get().getTarget()).isEqualTo(용산역);
        });
    }

    @Test
    @DisplayName("사용자 id로 즐겨찾기 목록 조회")
    void findAllByOwner() {
        Station 남영역 = stationRepository.findById(3L).get();
        favoriteRepository.save(Favorite.of(서울역, 용산역, member));
        favoriteRepository.save(Favorite.of(서울역, 남영역, member));

        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(member.getId());

        assertAll(() -> {
            assertThat(favorites.size()).isEqualTo(2);
            assertThat(favorites.get(0).getSource()).isEqualTo(서울역);
            assertThat(favorites.get(0).getTarget()).isEqualTo(용산역);
        });
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void delete() {
        Favorite persist = favoriteRepository.save(Favorite.of(서울역, 용산역, member));

        Optional<Favorite> find = favoriteRepository.findById(persist.getId());

        assertTrue(find.isPresent());

        favoriteRepository.delete(find.get());

        List<Favorite> favorites = favoriteRepository.findAllByOwnerId(member.getId());

        assertThat(favorites.size()).isEqualTo(0);
    }

    @Test
    void deleteMember() {
        member = memberRepository.findById(1L).get();
        Favorite persist = favoriteRepository.save(Favorite.of(서울역, 용산역, member));

        Optional<Favorite> find = favoriteRepository.findById(persist.getId());

        assertTrue(find.isPresent());

//        favoriteRepository.deleteAll();
        try {
            memberRepository.deleteAll();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }


        List<Member> all = memberRepository.findAll();

        System.out.println(all.size());


    }
}
