package xyz.mgurga.jitter.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import xyz.mgurga.jitter.utils.Tweet;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {}