package com.eureka.urllookup.urllookup.repository;

import com.eureka.urllookup.urllookup.model.PrettyUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface URLMapRepository extends JpaRepository<PrettyUrl, Long> {
    @Query("from pretty_url p where p.path like :pathOrPretty% or p.pretty like :pathOrPretty%")
    public List<PrettyUrl> findURLMap(@Param("pathOrPretty") String pathOrPretty);
}
