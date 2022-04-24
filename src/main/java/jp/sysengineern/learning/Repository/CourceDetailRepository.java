package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.CourceDetail;
import jp.sysengineern.learning.Entity.CourceTitle;

@Repository
public interface CourceDetailRepository extends JpaRepository<CourceDetail,Long> {
    List<CourceDetail> findAll();

    CourceDetail findByUrl(String detailUrl);

    CourceDetail findByCourceDetailId(Long courceDetailId);

    List<CourceDetail> findByCourceTitle(CourceTitle courceTitle);


}
