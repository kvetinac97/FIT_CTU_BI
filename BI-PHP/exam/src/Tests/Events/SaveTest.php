<?php declare(strict_types=1);

namespace FinalTask\Tests\Events;

use FinalTask\Events\Event;

class SaveTest extends AbstractEventsTest
{
    public function setUp(): void
    {
        parent::setUp();
        Event::createDbTable();
    }

    public function testInsert(): void
    {
        $event = $this->createEvent('Beatles revival', '2019-01-02', 'O2 Arena');
        $stmt = $this->pdo->prepare('SELECT * FROM events WHERE title=:title');
        $stmt->execute(['title' => $event->getTitle()]);
        $rows = $stmt->fetchAll(\PDO::FETCH_ASSOC);
        $this->assertCount(1, $rows);
    }

    public function testInsertWithId(): void
    {
        $event = $this->createEvent('Beatles revival', '2019-01-02', 'O2 Arena', 123);
        $stmt = $this->pdo->prepare('SELECT * FROM events WHERE title=:title');
        $stmt->execute(['title' => $event->getTitle()]);
        $rows = $stmt->fetchAll(\PDO::FETCH_ASSOC);
        $this->assertCount(1, $rows);
    }

    public function testUpdate(): void
    {
        $event = $this->createEvent('Vypsana propisovacka', '2018-12-23', 'Lucerna', 1);
        $event->setTitle('Vypsana fiXa');
        $event->save();

        $stmt = $this->pdo->prepare('SELECT * FROM events WHERE title=:title');
        $stmt->execute(['title' => $event->getTitle()]);
        $rows = $stmt->fetchAll(\PDO::FETCH_ASSOC);
        $this->assertCount(1, $rows);
        $row = current($rows);
        $this->assertEquals($event->getTitle(), $row['title']);
    }

    public function testOverwrite(): void
    {
        $old = $this->createEvent('Vypsana propisovacka', '2018-12-23', 'Lucerna', 1);
        $new = $this->createEvent('Vypsana fiXa', '2018-12-23', 'Lucerna', 1);

        $stmt = $this->pdo->prepare('SELECT * FROM events WHERE title=:title');
        $stmt->execute(['title' => $new->getTitle()]);
        $rows = $stmt->fetchAll(\PDO::FETCH_ASSOC);
        $this->assertCount(1, $rows);
        $row = current($rows);
        $this->assertEquals($new->getTitle(), $row['title']);

        $stmt = $this->pdo->prepare('SELECT * FROM events WHERE title=:title');
        $stmt->execute(['title' => $old->getTitle()]);
        $rows = $stmt->fetchAll(\PDO::FETCH_ASSOC);
        $this->assertCount(0, $rows);
    }
}
