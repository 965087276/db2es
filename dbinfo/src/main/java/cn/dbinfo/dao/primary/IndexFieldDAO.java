package cn.dbinfo.dao.primary;

import cn.db2es.common.pojo.primary.IndexField;
import cn.db2es.common.pojo.primary.IndexInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndexFieldDAO extends JpaRepository<IndexField, Integer> {
    List<IndexField> findByFather(IndexField father);
    List<IndexField> findByIndexInfo(IndexInfo indexInfo);
    IndexField findByIndexInfoAndTableField(IndexInfo indexInfo, String tableField);
    void deleteByIndexInfo(IndexInfo indexInfo);
}
