package com.capgemini.estimate.poc.estimate_api.domain.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.capgemini.estimate.poc.estimate_api.domain.model.Estimate;

@Repository
public interface EstimateRepository {
  List<Estimate> selectAll();
}
