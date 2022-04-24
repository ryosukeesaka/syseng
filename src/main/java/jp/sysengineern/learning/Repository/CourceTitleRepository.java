package jp.sysengineern.learning.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.sysengineern.learning.Entity.CourceTitle;

@Repository
public interface CourceTitleRepository extends JpaRepository<CourceTitle, Long> {
	List<CourceTitle> findAll();

	CourceTitle findByUrl(String titleUrl);
}
