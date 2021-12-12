package nextstep.subway.line.domain;

import nextstep.subway.path.infrastructure.SubwayUser;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SubwayUserTest
 * author : haedoang
 * date : 2021/12/09
 * description :
 */
class SubwayUserTest {

    @Test
    @DisplayName("지하철 사용자 구분하기")
    public void subwayUser() throws Exception {
        //given
        final Member infant = new Member("infant@gmail.com", "11", 3);
        final Member child = new Member("child@gmail.com", "11", 6);
        final Member youth = new Member("child@gmail.com", "11", 17);
        final Member adult = new Member("child@gmail.com", "11", 20);

        //when
        final SubwayUser infantUser = SubwayUser.of(infant.getAge());
        final SubwayUser childUser = SubwayUser.of(child.getAge());
        final SubwayUser youthUser = SubwayUser.of(youth.getAge());
        final SubwayUser adultUser = SubwayUser.of(adult.getAge());

        //then
        assertThat(infantUser.isPayUser()).isFalse();
        assertThat(childUser.isPayUser()).isTrue();
        assertThat(youthUser.isPayUser()).isTrue();
        assertThat(adultUser.isPayUser()).isTrue();
    }
}