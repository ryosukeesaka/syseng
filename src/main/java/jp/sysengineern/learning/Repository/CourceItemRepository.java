package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.CourceDetail;
import jp.sysengineern.learning.Entity.CourceItem;
@Repository
public interface CourceItemRepository extends JpaRepository<CourceItem,Long> {
    List<CourceItem> findAll();

    List<CourceItem> findByCourceDetail(CourceDetail courceDetail);

    CourceItem findTop1ByCourceDetail(CourceDetail courceDetail);
    CourceItem findByUrl(String url);

    CourceItem findByCourceItemId(Long progressId);
}
