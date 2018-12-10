package cn.dbinfo.service.primary;

import cn.dbinfo.pojo.primary.IndexField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexFieldServiceTest {
    @Autowired
    IndexFieldService indexFieldService;
    @Autowired
    IndexInfoService indexInfoService;

    @Test
    public void addTest() {
        IndexField pre = null;
        for (int i = 0; i < 10; i++) {
            IndexField now = new IndexField();
            if (pre != null) now.setFather(pre);
            now.setName("field" + i);
            indexFieldService.add(now);
            pre = now;
        }
    }

    @Test
    public void updateTest() {

    }

    @Test
    public void listByFatherTest() {
        IndexField father = indexFieldService.get(5);
        List<IndexField> children = indexFieldService.listByFather(father);
        IndexField child = children.get(0);

//        System.out.println(child.getFather().getIndexInfo());
//        System.out.println(child.getChildren() == null);
    }

    @Test
    public void deleteTest() {
        for (int i = 3; i <= 10; i++) {
            IndexField indexField = indexFieldService.get(i);
            indexFieldService.delete(indexField);
//            indexFieldService.delete(indexField);
        }
    }
}
