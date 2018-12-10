package cn.dbinfo.dao.primary;

import cn.dbinfo.pojo.primary.TableConstraint;
import cn.dbinfo.pojo.primary.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableConstraintDAO extends JpaRepository<TableConstraint, Integer> {
    List<TableConstraint> findByTableInfo(TableInfo tableInfo);
    List<TableConstraint> findByTableInfoAndFkTableNotNull(TableInfo tableInfo);
    void deleteByTableInfo(TableInfo tableInfo);

}
