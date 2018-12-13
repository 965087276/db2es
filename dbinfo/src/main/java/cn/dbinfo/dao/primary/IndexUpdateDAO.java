package cn.dbinfo.dao.primary;


import cn.db2es.common.pojo.primary.IndexInfo;
import cn.db2es.common.pojo.primary.IndexUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IndexUpdateDAO extends JpaRepository<IndexUpdate, Integer> {
    void deleteByIndexInfoOrUpdateIndex(IndexInfo indexInfo, String updateIndex);
    List<IndexUpdate> findByIndexInfo(IndexInfo indexInfo);
    @Query(value = "select distinct t.update_index from index_update t", nativeQuery = true)
    List<Object> findIndexHadFk();
}
