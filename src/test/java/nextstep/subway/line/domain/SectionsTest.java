package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {

    private static Line 신분당선;
    Station 강남역;
    Station 양재역;
    int 강남역에서_양재역_거리;
    Section 강남역에서_양재역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-300");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        강남역에서_양재역_거리 = 10;
        Section 강남역에서_양재역 = new Section(신분당선, 강남역, 양재역, 강남역에서_양재역_거리);
        신분당선.addSection(강남역에서_양재역);
    }

    @DisplayName("이미 등록된 구간 추가")
    @Test
    void addDuplicateSection() {
        assertThrows(RuntimeException.class, () -> {
            신분당선.addSection(강남역에서_양재역);
        }, "이미 등록된 구간 입니다.");
    }

    @DisplayName("등록이 불가능한 구간 추가")
    @Test
    void addInvalidSection() {
        Station 신논현역 = new Station("신논현역");
        Station 판교역 = new Station("판교역");
        int 신논현역에서_판교역_거리 = 5;
        Section 신논현역에서_판교역 = new Section(신분당선, 신논현역, 판교역, 신논현역에서_판교역_거리);
        assertThrows(RuntimeException.class, () -> {
            신분당선.addSection(신논현역에서_판교역);
        }, "등록할 수 없는 구간 입니다.");
    }

}
