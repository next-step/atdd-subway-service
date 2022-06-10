package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("즐겨찾기 관련 레포지토리 테스트")
@DataJpaTest
class FavoriteRepositoryTest {

    private Station 강남 = new Station("강남");
    private Station 교대 = new Station("교대");
    private Station 신도림 = new Station("신도림");
    private Station 잠실 = new Station("잠실");
    private Member 사용자 = new Member("test@unit.com","1234",15);

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp(){
        memberRepository.save(사용자);
        stationRepository.saveAll(Arrays.asList(강남, 교대, 신도림, 잠실));
    }

    @DisplayName("즐겨찾기을 생성한다.")
    @Test
    void create(){

        //when
        Favorite favorite = new Favorite(강남, 교대, 사용자);
        favoriteRepository.save(favorite);

        //then
        assertAll(
                ()-> assertThat(favorite.getId()).isNotNull(),
                ()-> assertThat(favorite.getSource()).isEqualTo(강남),
                ()-> assertThat(favorite.getTarget()).isEqualTo(교대),
                ()-> assertThat(favorite.getMember()).isEqualTo(사용자)
        );
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAll(){
        //given
        favoriteRepository.saveAll(Arrays.asList(new Favorite(강남, 교대, 사용자), new Favorite(신도림, 잠실, 사용자)));

        //when
        List<Favorite> favorites = favoriteRepository.findAllByMember(사용자);

        //then
        assertAll(
                ()-> assertThat(favorites.get(0).getSource()).isEqualTo(강남),
                ()-> assertThat(favorites.get(0).getTarget()).isEqualTo(교대),
                ()-> assertThat(favorites.get(0).getMember()).isEqualTo(사용자),

                ()-> assertThat(favorites.get(1).getSource()).isEqualTo(신도림),
                ()-> assertThat(favorites.get(1).getTarget()).isEqualTo(잠실),
                ()-> assertThat(favorites.get(1).getMember()).isEqualTo(사용자)
        );
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void delete(){
        //given
        Favorite save1 = favoriteRepository.save(new Favorite(강남, 교대, 사용자));
        Favorite save2 = favoriteRepository.save(new Favorite(신도림, 잠실, 사용자));

        //when
        favoriteRepository.deleteById(save1.getId());

        //then
        List<Favorite> favorites = favoriteRepository.findAllByMember(사용자);
        assertAll(
                ()-> assertThat(favorites.get(0).getSource()).isEqualTo(신도림),
                ()-> assertThat(favorites.get(0).getTarget()).isEqualTo(잠실),
                ()-> assertThat(favorites.get(0).getMember()).isEqualTo(사용자)
        );
    }

    @DisplayName("사용자의 즐겨찾기 목록을 조회한다.")
    @Test
    void findAllByMember() {
        //given
        Favorite save1 = favoriteRepository.save(new Favorite(강남, 교대, 사용자));
        Favorite save2 = favoriteRepository.save(new Favorite(신도림, 잠실, 사용자));
        favoriteRepository.saveAll(Arrays.asList(save1,save2));

        //when
        List<Favorite> favorites = favoriteRepository.findAllByMember(사용자);

        //then
        assertAll(
                ()-> assertThat(favorites.get(0).getSource()).isEqualTo(강남),
                ()-> assertThat(favorites.get(0).getTarget()).isEqualTo(교대),
                ()-> assertThat(favorites.get(0).getMember()).isEqualTo(사용자),

                ()-> assertThat(favorites.get(1).getSource()).isEqualTo(신도림),
                ()-> assertThat(favorites.get(1).getTarget()).isEqualTo(잠실),
                ()-> assertThat(favorites.get(1).getMember()).isEqualTo(사용자)
        );

    }
}
