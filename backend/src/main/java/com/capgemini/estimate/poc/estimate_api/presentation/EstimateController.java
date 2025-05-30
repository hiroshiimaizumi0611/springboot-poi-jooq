package com.capgemini.estimate.poc.estimate_api.presentation;

import com.capgemini.estimate.poc.estimate_api.domain.model.Estimate;
import com.capgemini.estimate.poc.estimate_api.usecase.DownloadEstimateExcelUseCase;
import com.capgemini.estimate.poc.estimate_api.usecase.EstimateUseCase;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

  private final DownloadEstimateExcelUseCase downloadUseCase;
  private final EstimateUseCase estimateUseCase;

  public EstimateController(
      DownloadEstimateExcelUseCase downloadUseCase, EstimateUseCase estimateUseCase) {
    this.downloadUseCase = downloadUseCase;
    this.estimateUseCase = estimateUseCase;
  }

  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadExcel() {
    byte[] file = downloadUseCase.execute();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estimates.xlsx")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(file);
  }

  @GetMapping
  public List<Estimate> getEstimates() {
    return estimateUseCase.getAllEstimates();
  }

  @PostMapping
  public void addEstimate(@RequestBody Estimate estimate) {
    estimateUseCase.addEstimate(estimate);
  }
}
