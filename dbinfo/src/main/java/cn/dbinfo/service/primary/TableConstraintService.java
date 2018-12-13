package cn.dbinfo.service.primary;

import cn.db2es.common.pojo.primary.TableConstraint;
import cn.db2es.common.pojo.primary.TableInfo;

import java.util.List;

public interface TableConstraintService {
    List<TableConstraint> listByTableInfo(TableInfo tableInfo);
    List<TableConstraint> findFkByTableInfo(TableInfo tableInfo);
    void deleteByTableInfo(TableInfo tableInfo);

    void addAll(List<TableConstraint> tableConstraints);
}
