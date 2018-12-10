package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.IndexField;
import cn.dbinfo.pojo.primary.IndexInfo;

import java.util.List;

public interface IndexFieldService {
    void add(IndexField indexField);
    void addAll(List<IndexField> indexFields);
    void update(IndexField indexField);
    void delete(IndexField indexField);
    IndexField get(int id);
    IndexField findByIndexInfoAndTableField(IndexInfo indexInfo, String tableField);
    List<IndexField> listByFather(IndexField father);
    List<IndexField> listByIndexInfo(IndexInfo indexInfo);

    void deleteByIndexInfo(IndexInfo indexInfo);
}
