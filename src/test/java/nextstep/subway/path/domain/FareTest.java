package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.GuestMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.utils.fixture.DomainLayerSubwayFixture;
import org.junit.jupiter.api.Test;

class FareTest {
    private DomainLayerSubwayFixture 지하철 = new DomainLayerSubwayFixture();

    private AuthMember 비회원 = new GuestMember();
    private AuthMember 성인 = new LoginMember(1L, "adult", AgeFarePolicy.TEENAGER_MAX_AGE.value() + 1);
    private AuthMember 청소년 = new LoginMember(2L, "teenager", AgeFarePolicy.TEENAGER_MIN_AGE.value());
    private AuthMember 어린이 = new LoginMember(3L, "kid", AgeFarePolicy.KID_MIN_AGE.value());

    private Path 추가요금_없는_노선_기본_경로 = new Path(new HashSet<>(Arrays.asList(지하철.신분당선)), 10);
    private Path 추가요금_없는_노선_16km_경로 = new Path(new HashSet<>(Arrays.asList(지하철.신분당선)), 16);
    private Path 추가요금_없는_노선_59km_경로 = new Path(new HashSet<>(Arrays.asList(지하철.신분당선)), 59);
    private Path 추가요금_있는_노선_59km_경로 = new Path(new HashSet<>(Arrays.asList(지하철.추가요금_노선)), 59);

    @Test
    void 비회원이_추가요금_없는_노선_기본_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_기본_경로, 비회원);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @Test
    void 성인이_추가요금_없는_노선_기본_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_기본_경로, 성인);

        // then
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @Test
    void 청소년이_추가요금_없는_노선_기본_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_기본_경로, 청소년);

        // then
        assertThat(fare.getFare()).isEqualTo(720);
    }

    @Test
    void 어린이가_추가요금_없는_노선_기본_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_기본_경로, 어린이);

        // then
        assertThat(fare.getFare()).isEqualTo(450);
    }

    @Test
    void 성인이_추가요금_없는_노선_16km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_16km_경로, 성인);

        // then
        assertThat(fare.getFare()).isEqualTo(1450);
    }

    @Test
    void 청소년이_추가요금_없는_노선_16km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_16km_경로, 청소년);

        // then
        assertThat(fare.getFare()).isEqualTo(880);
    }

    @Test
    void 어린이가_추가요금_없는_노선_16km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_16km_경로, 어린이);

        // then
        assertThat(fare.getFare()).isEqualTo(550);
    }

    @Test
    void 성인이_추가요금_없는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_59km_경로, 성인);

        // then
        assertThat(fare.getFare()).isEqualTo(2250);
    }

    @Test
    void 청소년이_추가요금_없는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_59km_경로, 청소년);

        // then
        assertThat(fare.getFare()).isEqualTo(1520);
    }

    @Test
    void 어린이가_추가요금_없는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_없는_노선_59km_경로, 어린이);

        // then
        assertThat(fare.getFare()).isEqualTo(950);
    }

    @Test
    void 성인이_추가요금_있는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_있는_노선_59km_경로, 성인);

        // then
        assertThat(fare.getFare()).isEqualTo(3150);
    }

    @Test
    void 청소년이_추가요금_있는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_있는_노선_59km_경로, 청소년);

        // then
        assertThat(fare.getFare()).isEqualTo(2240);
    }

    @Test
    void 어린이가_추가요금_있는_노선_59km_경로를_조회하면_요금이_올바르게_계산되어야_한다() {
        // when
        final Fare fare = new Fare(추가요금_있는_노선_59km_경로, 어린이);

        // then
        assertThat(fare.getFare()).isEqualTo(1400);
    }
}
