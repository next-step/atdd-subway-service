package nextstep.subway.fee.ui;

import nextstep.subway.fee.application.FeeService;
import nextstep.subway.fee.dto.FeeRequest;
import nextstep.subway.fee.dto.FeeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FeeController {

    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/fees")
    public ResponseEntity<FeeResponse> createFee(@RequestBody FeeRequest feeRequest) {
        FeeResponse fee = feeService.saveFee(feeRequest);
        return ResponseEntity.created(URI.create("/fees/" + fee.getId())).body(fee);
    }
}
