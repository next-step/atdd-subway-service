package nextstep.subway.fee.application;

import nextstep.subway.fee.domain.StationFee;
import nextstep.subway.fee.domain.StationFeeRepository;
import nextstep.subway.fee.dto.FeeRequest;
import nextstep.subway.fee.dto.FeeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeeService {
    private StationFeeRepository stationFeeRepository;

    public FeeService(StationFeeRepository stationFeeRepository) {
        this.stationFeeRepository = stationFeeRepository;
    }

    public FeeResponse saveFee(FeeRequest feeRequest) {
        StationFee savedFee = stationFeeRepository.save(feeRequest.toStationFee());
        return FeeResponse.of(savedFee);
    }
}
