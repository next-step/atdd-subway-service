package nextstep.subway.path.infrastructure;

import nextstep.subway.path.domain.policy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FarePoliciesConfig {
    @Bean
    public ChargeFarePolicy createChargeFarePolicy() {
        return new ChargeFareByDistancePolicy();
    }

    @Bean
    public AdditionalFarePolicy createAdditionalFarePolicy() {
        return new AdditionalFareByLinePolicy();
    }

    @Bean
    public DiscountPolicy createDiscountPolicy() {
        return new DiscountByAgePolicy();
    }
}
