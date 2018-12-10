package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.TableField;
import cn.dbinfo.pojo.primary.TableInfo;

import java.util.List;

public interface TableFieldService {
    void add(TableField tableField);
    void delete(TableField tableField);
    List<TableField> listByTableInfo(TableInfo tableInfo);
    TableField findByTableInfoAndName(TableInfo tableInfo, String name);
    void deleteByTableInfo(TableInfo tableInfo);
    void addAll(List<TableField> tableFields);
}
