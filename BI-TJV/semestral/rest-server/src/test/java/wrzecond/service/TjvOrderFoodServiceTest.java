package wrzecond.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import wrzecond.food.TjvFoodReadDTO;
import wrzecond.order.TjvOrderCreateDTO;
import wrzecond.order_food.TjvOrderFoodCreateDTO;
import wrzecond.order_food.TjvOrderFoodReadDTO;
import wrzecond.order.TjvOrderReadDTO;
import wrzecond.entity.*;
import wrzecond.repository.TjvOrderFoodRepository;
import wrzecond.table.TjvTableType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@SpringBootTest
public class TjvOrderFoodServiceTest {

    @Autowired
    private TjvOrderFoodService service;

    @MockBean
    private TjvOrderService orderService;
    @MockBean
    private TjvFoodService foodService;
    @MockBean
    private TjvTableService tableService;
    @MockBean
    private TjvOrderFoodRepository repository;

    @Test
    void createOrUpdateAt () {
        // Table
        TjvTable table = new TjvTable(TjvTableType.INSIDE),
                table2 = new TjvTable(TjvTableType.INSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(table2, "id", 1000 + (new Random()).nextInt(1000));

        // Table mock
        BDDMockito.given(tableService.findByType(TjvTableType.INSIDE)).willReturn(Arrays.asList(table, table2));

        // Order
        TjvOrder order1 = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(1)), false),
                order2 = new TjvOrder(table2, Timestamp.from(Instant.now().minusSeconds(3)), false);
        ReflectionTestUtils.setField(order1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(order2, "id", 1000 + (new Random()).nextInt(1000));

        TjvOrderReadDTO dto1 = new TjvOrderReadDTO(order1.getId(), table.getId(), order1.getDatetime(), false),
                dto2 = new TjvOrderReadDTO(order2.getId(), table2.getId(), order2.getDatetime(), false);
        TjvOrderCreateDTO cDto = new TjvOrderCreateDTO(table.getId(), order1.getDatetime(), false),
                cDto2 = new TjvOrderCreateDTO(table2.getId(), order2.getDatetime(), false);

        // Order mock
        BDDMockito.given(orderService.getEntityByID(order1.getId())).willReturn(order1);
        BDDMockito.given(orderService.getEntityByID(order2.getId())).willReturn(order2);
        BDDMockito.given(orderService.findByTableID(table.getId())).willReturn(Collections.singletonList(dto1));
        BDDMockito.given(orderService.findByTableID(table2.getId())).willReturn(Collections.emptyList());
        BDDMockito.given(orderService.create(BDDMockito.any(TjvOrderCreateDTO.class))).willReturn(dto2);

        // Food
        TjvFood food1 = new TjvFood("Hovezi polevka", 60, true, Collections.emptyList()),
                food2 = new TjvFood("Kofola", 140, false, Collections.emptyList()),
                food3 = new TjvFood("Kure na paprice", 230, true, Collections.emptyList());
        ReflectionTestUtils.setField(food1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(food2, "id", 1000 + (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(food3, "id", 2000 + (new Random()).nextInt(1000));

        // Food mock
        BDDMockito.given(foodService.getEntityByID(food1.getId())).willReturn(food1);
        BDDMockito.given(foodService.getEntityByID(food2.getId())).willReturn(food2);
        BDDMockito.given(foodService.getEntityByID(food3.getId())).willReturn(food3);

        // OrderFood
        TjvOrderFoodCreateDTO createDto1 = new TjvOrderFoodCreateDTO(order1.getId(), food1.getId(), Timestamp.from(Instant.now()), 1),
                createDto2 = new TjvOrderFoodCreateDTO(order2.getId(), food2.getId(), Timestamp.from(Instant.now().minusSeconds(4)), 2),
                createDto3 = new TjvOrderFoodCreateDTO(order2.getId(), food1.getId(), Timestamp.from(Instant.now().minusSeconds(2)), 3),
                createDto4 = new TjvOrderFoodCreateDTO(order2.getId(), food3.getId(), Timestamp.from(Instant.now().minusSeconds(5)), 4),
                createDto5 = new TjvOrderFoodCreateDTO(order2.getId(), food1.getId(), Timestamp.from(Instant.now().minusSeconds(12)), 5);
        TjvOrderFood orderFood1 = new TjvOrderFood(order1, food1, createDto1.getDatetime(), 1),
                orderFood2 = new TjvOrderFood(order1, food2, createDto2.getDatetime(), 2),
                orderFood3 = new TjvOrderFood(order2, food1, createDto1.getDatetime(), 1),
                orderFood4 = new TjvOrderFood(order2, food2, createDto2.getDatetime(), 2),
                orderFood5 = new TjvOrderFood(order2, food2, createDto3.getDatetime(), 3),
                orderFood6 = new TjvOrderFood(order2, food3, createDto4.getDatetime(), 4);

        // OrderFood mock
        BDDMockito.given(repository.save(orderFood1)).willReturn(orderFood1);
        BDDMockito.given(repository.save(orderFood2)).willReturn(orderFood2);
        BDDMockito.given(repository.save(orderFood3)).willReturn(orderFood3);
        BDDMockito.given(repository.save(orderFood4)).willReturn(orderFood4);

        // Test
        Assertions.assertEquals(
            Arrays.asList(dto1, dto2),
            service.createOrderAtTypeWithFood(TjvTableType.INSIDE, Arrays.asList(createDto1, createDto2))
        );

        BDDMockito.verify(tableService, BDDMockito.atLeastOnce()).findByType(TjvTableType.INSIDE);
        BDDMockito.verify(orderService, Mockito.atLeastOnce()).findByTableID(table.getId());
        BDDMockito.verify(orderService, Mockito.atLeastOnce()).findByTableID(table2.getId());
        BDDMockito.verify(orderService, BDDMockito.atLeast(2)).getEntityByID(BDDMockito.any());
        BDDMockito.verify(orderService, BDDMockito.atLeastOnce()).create(BDDMockito.any(TjvOrderCreateDTO.class));
        BDDMockito.verify(foodService, BDDMockito.atLeast(2)).getEntityByID(BDDMockito.any());
        BDDMockito.verify(repository, BDDMockito.atLeast(4)).save(BDDMockito.any());

        // Mock2
        BDDMockito.given(tableService.getEntityByID(table2.getId())).willReturn(table2);
        BDDMockito.given(repository.save(BDDMockito.any())).willReturn(orderFood3);
        BDDMockito.given(orderService.create(BDDMockito.anyList())).willReturn(Collections.singletonList(dto2));

        // Test2
        Assertions.assertEquals(dto2, service.createOrUpdateAtTable(table2.getId(), Arrays.asList(createDto2, createDto3)));
        BDDMockito.verify(tableService, BDDMockito.atLeastOnce()).getEntityByID(table2.getId());
        BDDMockito.verify(orderService, BDDMockito.atLeastOnce()).create(BDDMockito.any(TjvOrderCreateDTO.class));
        BDDMockito.verify(foodService, BDDMockito.atLeast(2)).getEntityByID(BDDMockito.any());
        BDDMockito.verify(repository, BDDMockito.atLeast(2)).save(BDDMockito.any());

        // Mock3
        BDDMockito.given(repository.findByTable(table2)).willReturn(Arrays.asList(orderFood4, orderFood5));
        BDDMockito.given(foodService.toDTO(food1)).willReturn(new TjvFoodReadDTO(food1.getId(), null, null, null, null));
        BDDMockito.given(foodService.toDTO(food2)).willReturn(new TjvFoodReadDTO(food2.getId(), null, null, null, null));
        BDDMockito.given(foodService.toDTO(food3)).willReturn(new TjvFoodReadDTO(food3.getId(), null, null, null, null));
        BDDMockito.given(orderService.getByID(order2.getId())).willReturn(dto2);

        // Test3
        Assertions.assertEquals(dto2, service.createOrUpdateAtTable(table2.getId(), Arrays.asList(createDto4, createDto5)));

        BDDMockito.verify(repository, Mockito.atLeastOnce()).findByTable(table2);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).deleteById(orderFood4.getId());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(orderFood6);
        BDDMockito.verify(repository, Mockito.atLeastOnce()).save(orderFood4);
        BDDMockito.verify(foodService, Mockito.atLeast(3)).toDTO(BDDMockito.any());
    }

    @Test
    void foodInOrder () {
        // Table
        TjvTable table1 = new TjvTable(TjvTableType.INSIDE), table2 = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(table2, "id", 1000 + (new Random()).nextInt(1000));

        // Table mock
        BDDMockito.given(tableService.getEntityByID(table1.getId())).willReturn(table1);
        BDDMockito.given(tableService.getEntityByID(table2.getId())).willReturn(table2);

        // Order
        TjvOrder order1 = new TjvOrder(table1, Timestamp.from(Instant.now().minusSeconds(1)), false),
                order2 = new TjvOrder(table2, Timestamp.from(Instant.now().minusSeconds(3)), false);
        ReflectionTestUtils.setField(order1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(order2, "id", 1000 + (new Random()).nextInt(1000));

        // Order mock
        BDDMockito.given(orderService.getEntityByID(order1.getId())).willReturn(order1);
        BDDMockito.given(orderService.getEntityByID(order2.getId())).willReturn(order2);

        // Food
        TjvFood food1 = new TjvFood("Hovezi polevka", 60, true, Collections.emptyList()),
                food2 = new TjvFood("Kofola", 140, false, Collections.emptyList());
        ReflectionTestUtils.setField(food1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(food2, "id", 1000 + (new Random()).nextInt(1000));

        TjvFoodReadDTO foodDto1 = new TjvFoodReadDTO(food1.getId(), food1.getName(), food1.getPrice(), food1.isCooked(), Collections.emptyList()),
                foodDto2 = new TjvFoodReadDTO(food2.getId(), food2.getName(), food2.getPrice(), food2.isCooked(), Collections.emptyList());

        // Food mock
        BDDMockito.given(foodService.toDTO(food1)).willReturn(foodDto1);
        BDDMockito.given(foodService.toDTO(food2)).willReturn(foodDto2);

        // OrderFood
        TjvOrderFood orderFood1 = new TjvOrderFood(order1, food1, Timestamp.from(Instant.now()), 1),
                orderFood2 = new TjvOrderFood(order1, food2, Timestamp.from(Instant.now().minusSeconds(4)), 3);
        TjvOrderFoodReadDTO dto1 = new TjvOrderFoodReadDTO(orderFood1.getId(), orderFood1.getOrder().getId(),
                foodDto1, orderFood1.getDatetime(), orderFood1.getCount());
        TjvOrderFoodReadDTO dto2 = new TjvOrderFoodReadDTO(orderFood2.getId(), orderFood2.getOrder().getId(),
                foodDto2, orderFood2.getDatetime(), orderFood2.getCount());

        // OrderFood mock
        BDDMockito.given(repository.findByTable(table1)).willReturn(Arrays.asList(orderFood1, orderFood2));
        BDDMockito.given(repository.findByTable(table2)).willReturn(Collections.emptyList());

        // Test
        Assertions.assertEquals(Arrays.asList(dto2, dto1), service.getFoodAtTable(table1.getId()));
        BDDMockito.verify(tableService, Mockito.atLeastOnce()).getEntityByID(table1.getId());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).findByTable(table1);

        Assertions.assertEquals(Collections.emptyList(), service.getFoodAtTable(table2.getId()));
        BDDMockito.verify(tableService, Mockito.atLeastOnce()).getEntityByID(table2.getId());
        BDDMockito.verify(repository, Mockito.atLeastOnce()).findByTable(table2);
    }

    @Test
    void toDTO () {
        TjvTable table = new TjvTable(TjvTableType.INSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt());
        TjvOrder order = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(35)), false);
        ReflectionTestUtils.setField(order, "id", (new Random()).nextInt());
        TjvFood food = new TjvFood("Kofola", 140, false, Collections.emptyList());
        ReflectionTestUtils.setField(food, "id", (new Random()).nextInt());

        TjvFoodReadDTO foodDto = new TjvFoodReadDTO(food.getId(), food.getName(), food.getPrice(), food.isCooked(), Collections.emptyList());
        BDDMockito.given(foodService.toDTO(food)).willReturn(foodDto);

        TjvOrderFood orderFood = new TjvOrderFood(order, food, Timestamp.from(Instant.now()), 2);
        ReflectionTestUtils.setField(orderFood, "id", (new Random()).nextInt());
        TjvOrderFoodReadDTO dto = new TjvOrderFoodReadDTO(orderFood.getId(), order.getId(),
                foodDto, orderFood.getDatetime(), 2);
        Assertions.assertEquals(dto, service.toDTO(orderFood));
    }

    @Test
    void fromDTO () {
        TjvTable table = new TjvTable(TjvTableType.OUTSIDE);
        ReflectionTestUtils.setField(table, "id", (new Random()).nextInt());
        TjvOrder order = new TjvOrder(table, Timestamp.from(Instant.now().minusSeconds(15)), false);
        ReflectionTestUtils.setField(order, "id", (new Random()).nextInt());
        TjvAllergen allergen = new TjvAllergen("Milk");
        ReflectionTestUtils.setField(allergen, "id", (new Random()).nextInt());
        TjvFood food = new TjvFood("Hovezi polevka", 60, true, Collections.singletonList(allergen));

        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(order.getId(), food.getId(),
                Timestamp.from(Instant.now().minusSeconds(1)), 5);
        TjvOrderFood orderFood = new TjvOrderFood(order, food, dto.getDatetime(), 5);

        // Mock definition
        BDDMockito.given(orderService.getEntityByID(order.getId())).willReturn(order);
        BDDMockito.given(foodService.getEntityByID(food.getId())).willReturn(food);

        // Test
        Assertions.assertEquals(orderFood, service.fromDTO(dto));
    }

    @Test
    void merge () {
        TjvTable table1 = new TjvTable(TjvTableType.OUTSIDE), table2 = new TjvTable(TjvTableType.BAR);
        ReflectionTestUtils.setField(table1, "id", (new Random()).nextInt(1000));
        ReflectionTestUtils.setField(table2, "id", 1000 + (new Random()).nextInt(1000));
        TjvOrder order1 = new TjvOrder(table1, Timestamp.from(Instant.now().minusSeconds(42)), false),
                 order2 = new TjvOrder(table2, Timestamp.from(Instant.now().minusSeconds(69)), false);
        TjvFood food1 = new TjvFood("Klobasa", 79, true, Collections.emptyList()),
                food2 = new TjvFood("Zelna polevka", 60, true, Collections.emptyList());

        TjvOrderFood orderFood = new TjvOrderFood(order1, food1, Timestamp.from(Instant.now().minusSeconds(3)), 60),
                    orderFood2 = new TjvOrderFood(order2, food1, Timestamp.from(Instant.now().minusSeconds(120)), 60),
                    orderFood3 = new TjvOrderFood(order2, food2, orderFood2.getDatetime(), 1);
        TjvOrderFoodCreateDTO dto = new TjvOrderFoodCreateDTO(order2.getId(), null, orderFood2.getDatetime(), null),
                dto2 = new TjvOrderFoodCreateDTO(null, food2.getId(), null, 1);

        // Mock definition
        BDDMockito.given(orderService.getEntityByID(order2.getId())).willReturn(order2);
        BDDMockito.given(foodService.getEntityByID(food2.getId())).willReturn(food2);

        // Test
        Assertions.assertEquals(orderFood2, service.merge(orderFood, dto));
        BDDMockito.verify(orderService, BDDMockito.atLeastOnce()).getEntityByID(order2.getId());

        Assertions.assertEquals(orderFood3, service.merge(orderFood2, dto2));
        BDDMockito.verify(foodService, BDDMockito.atLeastOnce()).getEntityByID(food2.getId());
    }

}
