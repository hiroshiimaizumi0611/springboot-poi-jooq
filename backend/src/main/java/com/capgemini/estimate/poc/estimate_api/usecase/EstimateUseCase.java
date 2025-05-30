package com.capgemini.estimate.poc.estimate_api.usecase;

import com.capgemini.estimate.poc.estimate_api.domain.model.Estimate;
import com.capgemini.estimate.poc.estimate_api.domain.repository.EstimateRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstimateUseCase {

  @Autowired private final EstimateRepository repository;

  public EstimateUseCase(EstimateRepository repository) {
    this.repository = repository;
  }

  public List<Estimate> getAllEstimates() {
    return repository.selectAll();
  }

  public void addEstimate(Estimate estimate) {
    repository.insert(estimate);
  }
}
