package nextstep.subway.line.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    private final Station 서울역 = new Station("서울역");
    private final Station 남영역 = new Station("남영역");
    private final Station 용산역 = new Station("용산역");

    @Test
    @DisplayName("상행-하행 구분 테스트")
    void isTheUpDownLine() {

        Section 서울_남영 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(10));

        assertAll(() -> {
            assertTrue(서울_남영.isTheUpLine(남영_용산));
            assertFalse(서울_남영.isTheDownLine(남영_용산));
            assertFalse(남영_용산.isTheUpLine(서울_남영));
            assertTrue(남영_용산.isTheDownLine(서울_남영));
        });
    }


    @Test
    @DisplayName("다른 구간 체크 테스트")
    void isDifferentSection() {

        Section 서울_남영 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 남영_용산 = Section.create(남영역, 용산역, Distance.valueOf(10));

        assertAll(() -> {
            assertFalse(서울_남영.equalsStations(남영_용산));
            assertFalse(남영_용산.equalsStations(서울_남영));
        });
    }

    @Test
    @DisplayName("같은 구간 체크 테스트")
    void isSameSection() {

        Section 서울_남영_5 = Section.create(서울역, 남영역, Distance.valueOf(5));
        Section 서울_남영_10 = Section.create(서울역, 남영역, Distance.valueOf(10));

        assertAll(() -> {
            assertTrue(서울_남영_5.equalsStations(서울_남영_10));
            assertTrue(서울_남영_10.equalsStations(서울_남영_5));
        });
    }
}
