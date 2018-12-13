package cn.dbinfo.dao.secondary;

import cn.db2es.common.pojo.secondary.ColumnResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColumnResultDAO extends JpaRepository<ColumnResult, String> {
    @Query(value = "select COLUMNS.COLUMN_NAME, COLUMNS.DATA_TYPE from COLUMNS where COLUMNS.TABLE_SCHEMA = ?1 and COLUMNS.TABLE_NAME = ?2", nativeQuery = true)
    List<Object[]> findByDatabaseAndTable(String database, String table);

    @Query(value = "select distinct COLUMNS.TABLE_NAME from COLUMNS where COLUMNS.TABLE_SCHEMA = ?1", nativeQuery = true)
    List<Object> findTableByDatabase(String database);
}
