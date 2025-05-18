package com.capgemini.estimate.poc.estimate_api.repository;

import com.capgemini.estimate.poc.estimate_api.domain.Estimate;
import java.util.List;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface EstimateRepository {
  @Select
  List<Estimate> selectAll();
}
