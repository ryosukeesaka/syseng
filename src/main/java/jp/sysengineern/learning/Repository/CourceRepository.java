package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.Cource;

@Repository
public interface CourceRepository extends JpaRepository<Cource,Long> {
    List<Cource> findAll();

    List<Cource> findByCourceId(Long courceid);

    Cource findByUrl(String url);

}
