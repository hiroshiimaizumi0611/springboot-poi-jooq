package com.capgemini.estimate.poc.estimate_api.repository;

import com.capgemini.estimate.poc.estimate_api.domain.Estimate;
import com.capgemini.estimate.poc.estimate_api.domain.Estimate_;
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
}
