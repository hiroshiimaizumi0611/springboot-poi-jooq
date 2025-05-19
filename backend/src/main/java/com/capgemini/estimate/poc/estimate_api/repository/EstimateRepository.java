package com.capgemini.estimate.poc.estimate_api.repository;

import com.capgemini.estimate.poc.estimate_api.domain.Estimate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimateRepository {
  List<Estimate> selectAll();
}
