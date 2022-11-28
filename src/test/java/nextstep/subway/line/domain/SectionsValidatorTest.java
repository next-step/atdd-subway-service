package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsValidatorTest {
    Station 왕십리;
    Station 신당;
    Station DDP;
    Station 을지로;

    @BeforeEach
    void setup() {
        왕십리 = Station.of(1L, "왕십리");
        신당 = Station.of(2L, "신당");
        DDP = Station.of(3L, "DDP");
        을지로 = Station.of(4L, "을지로");
    }

    @Test
    void validateAddNew_NOT_CONTAINS_ALL_MESSAGE() {
        // given
        Section section = Section.of(1L, 신당, DDP);
        Stations stations = Stations.of(Arrays.asList(왕십리, 을지로));

        // when
        // then
        assertThatThrownBy(() -> SectionsValidator.validateAddNew(section, stations))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SectionsValidator.NOT_CONTAINS_ALL_MESSAGE);
    }

    @Test
    void validateAddNew_ALREADY_EXISTED_MESSAGE() {
        // given
        Section section = Section.of(1L, 왕십리, 을지로);
        Stations stations = Stations.of(Arrays.asList(왕십리, 을지로));

        // when
        // then
        assertThatThrownBy(() -> SectionsValidator.validateAddNew(section, stations))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SectionsValidator.ALREADY_EXISTED_MESSAGE);
    }

    @Test
    void validateAddNew() {
        // given
        Section section = Section.of(1L, 왕십리, 을지로);
        Stations stations = Stations.of(Arrays.asList(왕십리, 신당));

        // when
        // then
        assertThatNoException().isThrownBy(() -> SectionsValidator.validateAddNew(section, stations));
    }
}
