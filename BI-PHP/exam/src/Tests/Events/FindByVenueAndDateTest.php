<?php declare(strict_types=1);

namespace FinalTask\Tests\Events;

use FinalTask\Events\Event;

class FindByVenueAndDateTest extends AbstractEventsTest
{
    public function setUp(): void
    {
        parent::setUp();
        Event::createDbTable();
    }

    public function testFindByVenue(): void
    {
        $events[] = $this->createEvent('Beatles revival', '2019-01-02', 'O2 Arena');
        $events[] = $this->createEvent('ABBA revival', '2019-01-03', 'O2 Arena');
        $events[] = $this->createEvent('Vypsana fiXa', '2018-12-23', 'Lucerna');
        $events[] = $this->createEvent('the.switch', '2018-10-01', 'Lucerna');
        $events[] = $this->createEvent('Arch Enemy', '2018-10-01', 'Lucerna');
        $events[] = $this->createEvent('DJ PHP', '2019-06-06', 'Psychiatrická léčebna Bohnice');

        $found = Event::findByVenueAndDate('Lucerna');
        $this->assertCount(3, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('O2 Arena');
        $this->assertCount(2, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('Psychiatrická léčebna Bohnice');
        $this->assertCount(1, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('Exit Chmelnice');
        $this->assertCount(0, $found);
    }

    public function testFindByVenueAndDate(): void
    {
        $events[] = $this->createEvent('Beatles revival', '2019-01-02', 'O2 Arena');
        $events[] = $this->createEvent('ABBA revival', '2019-01-03', 'O2 Arena');
        $events[] = $this->createEvent('Vypsana fiXa', '2018-12-23', 'Lucerna');
        $events[] = $this->createEvent('the.switch', '2018-10-01', 'Lucerna');
        $events[] = $this->createEvent('Arch Enemy', '2018-10-01', 'Lucerna');
        $events[] = $this->createEvent('DJ PHP', '2019-06-06', 'Psychiatrická léčebna Bohnice');

        $found = Event::findByVenueAndDate('Lucerna', '2018-10-01');
        $this->assertCount(2, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('Lucerna', '2018-12-23');
        $this->assertCount(1, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('O2 Arena', '2019-01-02');
        $this->assertCount(1, $found);
        $this->assertContainsOnlyInstancesOf(Event::class, $found);

        $found = Event::findByVenueAndDate('O2 Arena', '2019-01-04');
        $this->assertCount(0, $found);

        $found = Event::findByVenueAndDate('Psychiatrická léčebna Bohnice', '2018-10-01');
        $this->assertCount(0, $found);

        $found = Event::findByVenueAndDate('Exit Chmelnice', '2019-06-06');
        $this->assertCount(0, $found);
    }
}
