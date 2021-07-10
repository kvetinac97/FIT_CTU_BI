<?php

namespace HW\Tests;

use HW\Lib\LinkedList;
use PHPUnit\Framework\TestCase;

/**
 * @covers \HW\Lib\LinkedList
 * @covers \HW\Lib\LinkedListItem
 */
class LinkedListItemTest extends TestCase
{
    protected LinkedList $list;

    public function setUp(): void
    {
        parent::setUp();
        $this->list = new LinkedList();
    }

    /**
     * Test basic functionality of prepend operation,
     * mostly not crashing on null pointers and setting correctly first/last
     */
    public function testPrependFirstLast () {
        $this->assertNull($this->list->getFirst());
        $this->assertNull($this->list->getFirst());

        // 10
        $item1 = $this->list->prependList("10");
        $this->assertTrue($item1->equals($this->list->getFirst()));
        $this->assertTrue($item1->equals($this->list->getLast()));

        // 0 <-> 10
        $item2 = $this->list->prependList("0");
        $this->assertTrue($item2->equals($this->list->getFirst()));
        $this->assertTrue($item1->equals($this->list->getLast()));

        // 0 <-> 5 <-> 10
        $this->list->prependItem($item1, "5");
        $this->assertTrue($item2->equals($this->list->getFirst()));
        $this->assertTrue($item1->equals($this->list->getLast()));

        // -3 <-> 0 <-> 5 <-> 10
        $item3 = $this->list->prependItem($item2, "-3");
        $this->assertTrue($item3->equals($this->list->getFirst()));
        $this->assertTrue($item1->equals($this->list->getLast()));

        // x
        $ret = $this->list->setFirst(null);
        $this->assertSame($this->list, $ret);
        $this->assertNull($this->list->getFirst());
        $this->assertNull($this->list->getLast());
    }

    /**
     * Test basic functionality of append operation,
     * mostly not crashing on null pointers and setting correctly first/last
     */
    public function testAppendFirstLast () {
        $this->assertNull($this->list->getFirst());
        $this->assertNull($this->list->getFirst());

        // 0
        $item1 = $this->list->appendList("0");
        $this->assertSame($item1, $this->list->getFirst());
        $this->assertSame($item1, $this->list->getLast());

        // 0 <-> 10
        $item2 = $this->list->appendList("10");
        $this->assertSame($item2, $this->list->getLast());
        $this->assertSame($item1, $this->list->getFirst());

        // 0 <-> 5 <-> 10
        $this->list->appendItem($item1, "5");
        $this->assertSame($item2, $this->list->getLast());
        $this->assertSame($item1, $this->list->getFirst());

        // 0 <-> 5 <-> 10 <-> 12
        $item4 = $this->list->appendItem($item2, "12");
        $this->assertSame($item4, $this->list->getLast());
        $this->assertSame($item1, $this->list->getFirst());

        // 0 <-> 5 <-> 10 <-> 15
        $item4->setValue("15");
        $this->assertSame($this->list->getLast()->getValue(), "15");

        // 0 <-> 5 <–> 10
        $ret = $this->list->setLast(null);
        $this->assertSame($this->list, $ret);
        $this->assertSame($item2, $this->list->getLast());
        $this->assertSame($item1, $this->list->getFirst());
    }

    /**
     * Test complete functionality of prepend/append, setFirst, setLast
     * (correct order, sequence of many operations)
     * @depends testAppendFirstLast
     * @depends testPrependFirstLast
     */
    public function testLinked () {
        $_4 = $this->list->appendList("4");
        $_7 = $this->list->appendList("7");
        $_3 = $this->list->prependList("3");
        $_1 = $this->list->prependList("1");
        $_0 = $this->list->prependItem($_1, "0");
        $_5 = $this->list->prependItem($_7, "5");
        $_6 = $this->list->appendItem($_5, "6");
        $_2 = $this->list->appendItem($_1, "2");

        $this->assertSame($_0, $this->list->getFirst());
        $this->assertNull($_0->getPrev());
        $this->assertSame($_1, $_0->getNext());
        $this->assertSame($_0, $_1->getPrev());
        $this->assertSame($_2, $_1->getNext());
        $this->assertSame($_1, $_2->getPrev());
        $this->assertSame($_3, $_2->getNext());
        $this->assertSame($_2, $_3->getPrev());
        $this->assertSame($_4, $_3->getNext());
        $this->assertSame($_3, $_4->getPrev());
        $this->assertSame($_5, $_4->getNext());
        $this->assertSame($_4, $_5->getPrev());
        $this->assertSame($_6, $_5->getNext());
        $this->assertSame($_5, $_6->getPrev());
        $this->assertSame($_7, $_6->getNext());
        $this->assertSame($_6, $_7->getPrev());
        $this->assertNull($_7->getNext());
        $this->assertSame($_7, $this->list->getLast());

        $this->list->setLast(null);
        $this->assertSame($_6, $this->list->getLast());
        $this->assertNull($_6->getNext());

        $this->list->setFirst($_3);
        $this->assertSame($_3, $this->list->getFirst());
        $this->assertNull($_3->getPrev());
        $this->assertSame($_0, $_3->getNext());
        $this->assertSame($_4, $_2->getNext());
        $this->assertSame($_2, $_4->getPrev());

        $this->list->setLast($_5);
        $this->assertSame($_5, $this->list->getLast());
        $this->assertNull($_5->getNext());
        $this->assertSame($_6, $_5->getPrev());
        $this->assertSame($_5, $_6->getNext());
        $this->assertSame($_6, $_4->getNext());
        $this->assertSame($_4, $_6->getPrev());

        $this->list->setFirst(null);
        $this->assertNull($this->list->getFirst());
        $this->assertNull($this->list->getLast());
    }

    /**
     * Test edge case when there is just one item
     * (correct setting of first and last item)
     */
    public function testOneItem () {
        $this->list->prependList("1");
        $this->list->setLast(null);
        $this->assertNull($this->list->getFirst());
        $this->assertNull($this->list->getLast());
    }

}
