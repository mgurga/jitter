package xyz.mgurga.jitter.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.mgurga.jitter.utils.TAccount;

@Repository
public interface TAccountRepository extends CrudRepository<TAccount, Long> {}