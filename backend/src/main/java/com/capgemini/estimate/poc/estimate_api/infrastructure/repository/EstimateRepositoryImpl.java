package com.capgemini.estimate.poc.estimate_api.infrastructure.repository;

import com.capgemini.estimate.poc.estimate_api.domain.model.Estimate;
import com.capgemini.estimate.poc.estimate_api.domain.model.Estimate_;
import com.capgemini.estimate.poc.estimate_api.domain.repository.EstimateRepository;
import java.util.List;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.stereotype.Repository;

@Repository
public class EstimateRepositoryImpl implements EstimateRepository {
  private final QueryDsl queryDsl;

  public EstimateRepositoryImpl(QueryDsl queryDsl) {
    this.queryDsl = queryDsl;
  }

  @Override
  public List<Estimate> selectAll() {
    var e = new Estimate_();

    return queryDsl.from(e).fetch();
  }

  @Override
  public void insert(Estimate estimate) {
    var e = new Estimate_();

    queryDsl.insert(e).single(estimate).execute();
  }
}
