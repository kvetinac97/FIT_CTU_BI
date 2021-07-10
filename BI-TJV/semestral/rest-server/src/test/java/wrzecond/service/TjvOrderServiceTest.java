package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.TjvOrder;
import wrzecond.entity.TjvTable;
import wrzecond.repository.TjvOrderRepository;
import wrzecond.table.TjvTableType;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@SpringBootTest
public class TjvOrderServiceTest {

    @Autowired
    private TjvOrderService service;

    @MockBean
    private TjvOrderRepository orderRepository;
    @MockBean
    private TjvTableService tableService;

    @Test
    void findByTableID () {
        TjvTable table = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt(1000));
        TjvOrder order1 = new TjvOrder(table, Timestamp.from(Instant.now()), false),
                 order2 = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(60)), false),
                 order3 = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(71)), true);
        ReflectionTestUtils.setField(order1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(order2, "id", 1000 + (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(order3, "id", 2000 + (new Random()).nextInt(1000));
        TjvOrderReadDTO dto1 = new TjvOrderReadDTO(order1.getId(), table.getId(), order1.getDatetime(), false),
                        dto2 = new TjvOrderReadDTO(order2.getId(), table.getId(), order2.getDatetime(), false);

        // Mock definition
        BDDMockito.given(tableService.getEntityByID(table.getId())).willReturn(table);
        BDDMockito.given(orderRepository.findByTableID(table)).willReturn(new ArrayList<TjvOrder>(){{
            add(order1); add(order2);
        }});

        // Test working
        Assertions.assertEquals(service.findByTableID(table.getId()), Arrays.asList(dto1, dto2));
        BDDMockito.verify(orderRepository, Mockito.atLeastOnce()).findByTableID(table);
        BDDMockito.verify(tableService, Mockito.atLeastOnce()).getEntityByID(table.getId());

        // Test exception
        EntityNotFoundException ex = null;
        try { service.findByTableID(1000); }
        catch ( EntityNotFoundException x ) { ex = x; }
        Assertions.assertNotNull(ex);
    }

    @Test
    void toDTO () {
        TjvTable table = new TjvTable(TjvTableType.INSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt());
        TjvOrder order = new TjvOrder(table, Timestamp.from(Instant.now()), false);
        ReflectionTestUtils.setField(order, "id", (new Random()).nextInt());
        TjvOrderReadDTO dto = new TjvOrderReadDTO(order.getId(), table.getId(), order.getDatetime(), false);
        Assertions.assertEquals(dto, service.toDTO(order));
    }

    @Test
    void fromDTO () {
        TjvTable table = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt());
        TjvOrder order = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(15)), true);
        TjvOrderCreateDTO dto = new TjvOrderCreateDTO(table.getId(), order.getDatetime());
        ReflectionTestUtils.setField(dto, "completed", true);

        // Mock definition
        BDDMockito.given(tableService.getEntityByID(table.getId())).willReturn(table);

        // Test
        Assertions.assertEquals(order, service.fromDTO(dto));
        BDDMockito.verify(tableService, BDDMockito.atLeastOnce()).getEntityByID(table.getId());
    }

    @Test
    void merge () {
        TjvTable table1 = new TjvTable(TjvTableType.BAR), table2 = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(table2, "id", 1000 + (new Random()).nextInt(1000));
        TjvOrder order = new TjvOrder(table1, Timestamp.from(Instant.now().minusSeconds(24 * 3600)), false),
                 order2 = new TjvOrder(table2, order.getDatetime(), false),
                 order3 = new TjvOrder(table2, Timestamp.from(Instant.now().minusSeconds(3600)), true);

        TjvOrderCreateDTO dto = new TjvOrderCreateDTO(table2.getId(), order.getDatetime(), false),
                dto2 = new TjvOrderCreateDTO(null, order3.getDatetime(), true),
                dto3 = new TjvOrderCreateDTO(null, null, null);

        // Mock definition
        BDDMockito.given(tableService.getEntityByID(table2.getId())).willReturn(table2);

        // Test
        Assertions.assertEquals(order2, service.merge(order, dto));
        BDDMockito.verify(tableService, BDDMockito.atLeastOnce()).getEntityByID(table2.getId());

        Assertions.assertEquals(order3, service.merge(order2, dto2));
        Assertions.assertEquals(order3, service.merge(order3, dto3));
    }

}
