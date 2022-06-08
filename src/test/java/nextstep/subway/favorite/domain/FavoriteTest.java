package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("즐겨찾기 관련 단위테스트")
@DataJpaTest
class FavoriteTest {
    
    private Station 강남 = new Station("강남");
    private Station 교대 = new Station("교대");
    
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @BeforeEach
    void setUp(){
        stationRepository.saveAll(Arrays.asList(강남, 교대));
    }
    
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void create(){

        //when
        Favorite favorite = new Favorite(강남, 교대);
        favoriteRepository.save(favorite);

        //then
        assertAll(
                ()-> assertThat(favorite.getId()).isNotNull(),
                ()-> assertThat(favorite.getSource()).isEqualTo(강남),
                ()-> assertThat(favorite.getTarget()).isEqualTo(교대)
        );
    }
}
