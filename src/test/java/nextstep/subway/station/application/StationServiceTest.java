package nextstep.subway.station.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 역 서비스")
@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository repository;

    @InjectMocks
    private StationService service;

    @Test
    @DisplayName("저장")
    void saveStation() {
        // given
        Name 강남 = Name.from("강남");
        지하철_역_이름이_중복되지_않음(강남);
        저장된_지하철_역_반환(Station.from(강남));

        // when
        service.saveStation(new StationRequest(강남.toString()));

        // then
        지하철_역_저장_요청됨(강남);
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 저장")
    void saveStation_alreadyExistsName_thrownDataIntegrityViolationException() {
        // given
        String 강남 = "강남";
        지하철_역_이름이_이미_존재(강남);

        // when
        ThrowingCallable saveCall = () -> service.saveStation((new StationRequest(강남)));

        // then
        중복_예외_발생(saveCall);
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 역을 찾기")
    void findById() {
        // given
        지하철_역_존재하지_않음();

        // when
        ThrowingCallable findByIdCallable = () -> service.findById(Long.MAX_VALUE);

        // then
        찾을_수_없는_예외_발생(findByIdCallable);
    }

    private void 지하철_역_저장_요청됨(Name expectedName) {
        ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
        verify(repository, times(1)).save(captor.capture());
        Station actual = captor.getValue();

        assertThat(actual.name())
            .isEqualTo(expectedName);
    }

    private void 중복_예외_발생(ThrowingCallable callable) {
        assertThatExceptionOfType(DuplicateDataException.class)
            .isThrownBy(callable)
            .withMessageEndingWith("이미 존재합니다.");
    }

    private void 찾을_수_없는_예외_발생(ThrowingCallable callable) {
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(callable)
            .withMessageEndingWith(" 존재하지 않습니다.");
    }

    private void 저장된_지하철_역_반환(Station station) {
        when(repository.save(any(Station.class)))
            .thenReturn(station);
    }

    private void 지하철_역_존재하지_않음() {
        when(repository.findById(anyLong()))
            .thenReturn(Optional.empty());
    }

    private void 지하철_역_이름이_이미_존재(String name) {
        when(repository.existsByName(Name.from(name)))
            .thenReturn(true);
    }

    private void 지하철_역_이름이_중복되지_않음(Name name) {
        when(repository.existsByName(name))
            .thenReturn(false);
    }
}
