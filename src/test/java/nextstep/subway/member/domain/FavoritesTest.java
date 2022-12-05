package nextstep.subway.member.domain;

import nextstep.subway.JpaEntityTest;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버의 일급컬렉션 - Favorites 도메인 테스트")
public class FavoritesTest extends JpaEntityTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    private Station 강남역;
    private Station 양재역;
    private Member 멤버;
    private Favorite 즐겨찾기;


    @BeforeEach
    public void setUp() {
        super.setUp();
        // 역생성
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        stationRepository.saveAll(Lists.newArrayList(강남역, 양재역));

        // 멤버 생성
        멤버 = new Member("email@email.com", "1234", 11);
        memberRepository.save(멤버);

        // favorite 생성
        즐겨찾기 = new Favorite(멤버, 강남역, 양재역);
        favoriteRepository.save(즐겨찾기);
        flushAndClear();
    }

    @DisplayName("즐겨찾기 리스트를 멤버에서부터 가져오기")
    @Test
    void getFavorites() {
        // when : 멤버를 호출 할 때
        Member member = memberRepository.getById(멤버.getId());

        // then : 즐겨찾기가 같이 검색되는 지 확인
        List<Favorite> favorites = member.getFavorites();
        assertThat(favorites).hasSize(1);
    }
}
