<?php declare(strict_types=1);

namespace FinalTask\Tests\Events;

use FinalTask\Events\Event;
use PDO;
use PHPUnit\Framework\TestCase;

abstract class AbstractEventsTest extends TestCase
{
    /** @var \PDO */
    protected $pdo;

    public function setUp(): void
    {
        $this->pdo = new PDO('sqlite::memory:');
        $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        Event::setDb($this->pdo);
    }

    protected function createEvent($title, $date, $venue, $id = null): Event
    {
        $event = new Event();
        $event->setTitle($title);
        $event->setDate($date);
        $event->setVenue($venue);
        $event->setId($id);
        $event->save();

        return $event;
    }
}
