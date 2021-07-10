<?php declare(strict_types=1);

namespace FinalTask\Tests\Events;

use FinalTask\Events\Event;

class CreateDbTableTest extends AbstractEventsTest
{
    public function setUp(): void
    {
        parent::setUp();
        Event::createDbTable();
    }

    public function testCreateDbTable(): void
    {
        $q = $this->pdo->prepare('PRAGMA table_info(events)');
        $q->execute();
        $fields = $q->fetchAll(\PDO::FETCH_ASSOC);

        $this->assertFieldExists('id', 'INTEGER', $fields);
        $this->assertFieldExists('title', 'TEXT', $fields);
        $this->assertFieldExists('date', 'TEXT', $fields);
        $this->assertFieldExists('venue', 'TEXT', $fields);
    }

    private function assertFieldExists($name, $type, $fields): void
    {
        $exists = false;

        foreach ($fields as $field) {
            if ($field['name'] === $name && $field['type'] === $type) {
                $exists = true;
            }
        }

        $this->assertTrue($exists, 'Field ' . $name . ' with type ' . $type . ' does not exist.');
    }
}
