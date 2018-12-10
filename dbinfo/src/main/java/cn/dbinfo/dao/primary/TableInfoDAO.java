package cn.dbinfo.dao.primary;

import cn.dbinfo.pojo.primary.IndexInfo;
import cn.dbinfo.pojo.primary.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TableInfoDAO extends JpaRepository<TableInfo, Integer> {
    TableInfo findByDatabaseNameAndTableName(String databaseName, String tableName);
    TableInfo findFirstByIndexInfo(IndexInfo indexInfo);
    List<TableInfo> findByDatabaseName(String databaseName);
    int countByIndexInfo(IndexInfo indexInfo);

    @Query(value = "select distinct t.database_name from table_info t", nativeQuery = true)
    List<Object> listDatabase();


}
