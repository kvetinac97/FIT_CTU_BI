package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.table.TjvTableCreateDTO;
import wrzecond.table.TjvTableReadDTO;
import wrzecond.entity.TjvTable;
import wrzecond.repository.TjvTableRepository;
import wrzecond.table.TjvTableType;

import java.util.Arrays;
import java.util.Random;

@SpringBootTest
public class TjvTableServiceTest {

    @Autowired
    private TjvTableService service;

    @MockBean
    private TjvTableRepository repository;

    @Test
    void findByType () {
        TjvTable table = new TjvTable(TjvTableType.OUTSIDE), table2 = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(table2, "id", 1000 + (new Random()).nextInt(1000));
        BDDMockito.given(repository.findByType(TjvTableType.OUTSIDE)).willReturn(Arrays.asList(table, table2));

        Assertions.assertEquals(Arrays.asList(table, table2), service.findByType(TjvTableType.OUTSIDE));
        BDDMockito.verify(repository, Mockito.atLeastOnce()).findByType(TjvTableType.OUTSIDE);
    }

    @Test
    void toDTO () {
        TjvTable table = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt());
        TjvTableReadDTO dto = new TjvTableReadDTO(table.getId(), TjvTableType.OUTSIDE);
        Assertions.assertEquals(dto, service.toDTO(table));
    }

    @Test
    void fromDTO () {
        TjvTableCreateDTO dto = new TjvTableCreateDTO(TjvTableType.INSIDE);
        TjvTable table = new TjvTable(TjvTableType.INSIDE);
        Assertions.assertEquals(table, service.fromDTO(dto));
    }

    @Test
    void merge () {
        TjvTable table = new TjvTable(TjvTableType.BAR), table2 = new TjvTable(TjvTableType.OUTSIDE);
        TjvTableCreateDTO dto = new TjvTableCreateDTO(null),
                dto2 = new TjvTableCreateDTO(TjvTableType.OUTSIDE);

        // Test
        Assertions.assertEquals(table, service.merge(table, dto));
        Assertions.assertEquals(table2, service.merge(table, dto2));
    }

}
