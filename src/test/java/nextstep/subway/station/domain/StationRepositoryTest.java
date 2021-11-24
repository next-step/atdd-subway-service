package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.domain.Name;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@DisplayName("지하철 역 저장소")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StationRepositoryTest {

    @Autowired
    private StationRepository repository;

    @Test
    @DisplayName("저장")
    void 지하철_역_저장() {
        // given
        Station 강남역 = Station.from(Name.from("강남"));

        // when
        Station savedStation = 지하철_역_저장(강남역);

        // then
        지하철_역_저장_됨(savedStation, 강남역.name());
    }

    @Test
    @DisplayName("중복된 이름으로 저장")
    void save_duplicationName_thrownDataIntegrityViolationException() {
        // given
        Name 강남 = Name.from("강남");
        지하철_역_저장_되어_있음(Station.from(강남));

        // when
        ThrowingCallable saveCallable = () -> 지하철_역_저장(Station.from(강남));

        // then
        assertThatExceptionOfType(DataIntegrityViolationException.class)
            .isThrownBy(saveCallable);
    }

    @ParameterizedTest(name = "[{index}] \"{0}\"은 이미 존재하는 이름이라는 사실이 {1}")
    @DisplayName("이미 존재하는 이름인지 확인")
    @CsvSource({"강남,true", "역삼,false"})
    void existsByName(String name, boolean expected) {
        // given
        지하철_역_저장_되어_있음(Station.from(Name.from("강남")));

        // when
        boolean existsByName = repository.existsByName(Name.from(name));

        // then
        assertThat(existsByName)
            .isEqualTo(expected);
    }

    private Station 지하철_역_저장(Station station) {
        return repository.save(station);
    }

    private void 지하철_역_저장_되어_있음(Station station) {
        지하철_역_저장(station);
    }

    private void 지하철_역_저장_됨(Station station, Name expectedName) {
        assertAll(
            () -> assertThat(station.id()).isNotNull(),
            () -> assertThat(station.name()).isEqualTo(expectedName),
            () -> assertThat(station.getCreatedDate()).isNotNull(),
            () -> assertThat(station.getModifiedDate()).isNotNull()
        );
    }
}
