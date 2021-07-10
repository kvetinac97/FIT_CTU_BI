<?php declare(strict_types=1);

namespace FinalTask\Tests\Events;

use FinalTask\Events\Event;

class FindByIdTest extends AbstractEventsTest
{
    public function setUp(): void
    {
        parent::setUp();
        Event::createDbTable();
    }

    public function testFindById(): void
    {
        $events[] = $this->createEvent('Beatles revival', '2019-01-02', 'O2 Arena', 123); // id 1
        $events[] = $this->createEvent('Vypsana fiXa', '2018-12-23', 'Lucerna', 2); // id 2
        $events[] = $this->createEvent('DJ PHP', '2019-06-06', 'Psychiatrická léčebna Bohnice'); // id 3

        foreach ($events as $event) {
            $found = Event::findById($event->getId());
            $this->assertInstanceOf(Event::class, $found);
            $this->assertEquals($event->getId(), $found->getId());
            $this->assertEquals($event->getTitle(), $found->getTitle());
            $this->assertEquals($event->getDate(), $found->getDate());
            $this->assertEquals($event->getVenue(), $found->getVenue());
        }
    }

    public function testFindNotExists(): void
    {
        $notFound = Event::findById(99);
        $this->assertNull($notFound);
    }
}
