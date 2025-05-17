package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.usecase.DownloadEstimateExcelUseCase;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

  private final DownloadEstimateExcelUseCase useCase;

  public EstimateController(DownloadEstimateExcelUseCase useCase) {
    this.useCase = useCase;
  }

  @GetMapping("/excel")
  public ResponseEntity<byte[]> downloadExcel() {
    byte[] file = useCase.execute();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estimates.xlsx")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(file);
  }
}
