package cn.dbinfo.service.primary;

import cn.db2es.common.pojo.primary.TableField;
import cn.db2es.common.pojo.primary.TableInfo;

import java.util.List;

public interface TableFieldService {
    void add(TableField tableField);
    void delete(TableField tableField);
    List<TableField> listByTableInfo(TableInfo tableInfo);
    TableField findByTableInfoAndName(TableInfo tableInfo, String name);
    void deleteByTableInfo(TableInfo tableInfo);
    void addAll(List<TableField> tableFields);
}
