package cn.dbinfo.dao.primary;

import cn.dbinfo.pojo.primary.TableField;
import cn.dbinfo.pojo.primary.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableFieldDAO extends JpaRepository<TableField, Integer> {
    List<TableField> findByTableInfo(TableInfo tableInfo);
    TableField findByTableInfoAndName(TableInfo tableInfo, String name);
    void deleteByTableInfo(TableInfo tableInfo);
}
