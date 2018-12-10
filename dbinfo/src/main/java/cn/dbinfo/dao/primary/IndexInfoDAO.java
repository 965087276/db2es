package cn.dbinfo.dao.primary;

import cn.dbinfo.pojo.primary.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoDAO extends JpaRepository<IndexInfo, Integer> {
    IndexInfo findByIndexName(String indexName);
}
