package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {

    private static final int DISTANCE = 10;

    private Line 신분당선;
    private Station 판교역;
    private Station 정자역;
    private Distance distance;

    @BeforeEach
    void setUp() {
        판교역 = Station.of(1L, "판교역");
        정자역 = Station.of(2L, "정자역");
        신분당선 = Line.of("신분당선", "빨강", 1000);
        distance = Distance.from(DISTANCE);
    }

    @DisplayName("상행역, 하행역, 구간거리 정보로 Section 을 생성한다.")
    @Test
    void create1() {
        // when
        Section 판교_정자_구간 = Section.of(신분당선, 판교역, 정자역, distance);

        // then
        assertAll(
            () -> assertEquals(판교_정자_구간.getUpStation(), 판교역),
            () -> assertEquals(판교_정자_구간.getDownStation(), 정자역),
            () -> assertTrue(판교_정자_구간.isSameDistance(distance))
        );
    }

    @DisplayName("상행역이 없으면 Section 생성 시, 예외가 발생한다.")
    @Test
    void create2() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Section.of(신분당선, null, 정자역, distance))
                                            .withMessageContaining("Section 생성에 필요한 필수 정보를 확인해주세요.");
    }

    @DisplayName("하행역이 없으면 Section 생성 시, 예외가 발생한다.")
    @Test
    void create3() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Section.of(신분당선, 판교역, null, distance))
                                            .withMessageContaining("Section 생성에 필요한 필수 정보를 확인해주세요.");
    }

    @DisplayName("구간거리가 없으면 Section 생성 시, 예외가 발생한다.")
    @Test
    void create4() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Section.of(신분당선, 판교역, 정자역, null))
                                            .withMessageContaining("Section 생성에 필요한 필수 정보를 확인해주세요.");
    }

    @DisplayName("상행역, 하행역이 같으면 예외가 발생한다.")
    @Test
    void create5() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Section.of(신분당선, 판교역, 판교역, distance))
                                            .withMessageContaining("상행역과 하행역은 같을 수 없습니다.");
    }
}