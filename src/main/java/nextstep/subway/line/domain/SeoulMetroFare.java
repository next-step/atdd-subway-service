package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.User;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Component;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : SeoulMetroFare
 * author : haedoang
 * date : 2021/12/12
 * description :
 */
@Component
public class SeoulMetroFare implements SubwayFare {
    @Override
    public Money rateInquiry(Path path, User user) {
        if (user.isStranger()) {
            return SeoulMetroType.rateInquiry(path.distance(), path.extraCharge());
        }
        return SeoulMetroType.rateInquiry(path.distance(), SubwayUser.of(user.getAge()), path.extraCharge());
    }
}
