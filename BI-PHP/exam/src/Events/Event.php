<?php

namespace FinalTask\Events;

/**
 * Class Event
 * @package FinalTask\Events
 */
class Event
{
    /** @var \PDO */
    protected static $pdo = null;
    private static int $entityCount = 1;

    private ?int $id;
    private ?string $title;
    private ?string $date;
    private ?string $venue;

    public function __construct ( ?int $id = null, ?string $title = null, ?string $date = null, ?string $venue = null ) {
        $this->id = $id;
        $this->title = $title;
        $this->date = $date;
        $this->venue = $venue;
    }

    /**
     * @param int|null $id
     */
    public function setId(?int $id): void
    {
        $this->id = $id;
    }

    /**
     * @param string|null $title
     */
    public function setTitle(?string $title): void
    {
        $this->title = $title;
    }

    /**
     * @param string|null $date
     */
    public function setDate(?string $date): void
    {
        $this->date = $date;
    }

    /**
     * @param string|null $venue
     */
    public function setVenue(?string $venue): void
    {
        $this->venue = $venue;
    }

    /**
     * @return int|null
     */
    public function getId(): ?int
    {
        return $this->id;
    }

    /**
     * @return string|null
     */
    public function getTitle(): ?string
    {
        return $this->title;
    }

    /**
     * @return string|null
     */
    public function getDate(): ?string
    {
        return $this->date;
    }

    /**
     * @return string|null
     */
    public function getVenue(): ?string
    {
        return $this->venue;
    }

    public static function setDb(\PDO $pdo): void
    {
        self::$pdo = $pdo;
    }

    public static function createDbTable(): void
    {
        self::$pdo->exec("
            CREATE TABLE IF NOT EXISTS events (
              id INTEGER PRIMARY KEY,
              title TEXT,
              date TEXT,
              venue TEXT
            )
        ");
    }

    public function save(): void
    {
        // Prepare params
        $params = ['title' => $this->title, 'date' => $this->date, 'venue' => $this->venue];

        // Found, update
        if ( self::findById($this->id) !== null ) {
            $params['id'] = $this->id;
            $statement2 = self::$pdo->prepare("UPDATE events SET title=:title, date=:date, venue=:venue WHERE id=:id");
            if ($statement2)
                $statement2->execute($params);

            return;
        }

        // Not found, create
        $statement3 = self::$pdo->prepare("INSERT INTO events (id, title, date, venue) VALUES (:id, :title, :date, :venue)");
        $params['id'] = self::$entityCount++;
        $statement3->execute($params);
    }

    /**
     * @param int|null $id
     * @return Event|null
     */
    public static function findById (?int $id): ?Event
    {
        $statement = self::$pdo->prepare('SELECT * FROM events WHERE id=:id');
        if ($id === null || !$statement || !$statement->execute(['id' => $id]))
            return null;

        $row = $statement->fetch(\PDO::FETCH_ASSOC);
        echo "Found with id $id row " . intval($row) . PHP_EOL;
        return $row !== false ? new Event($row["id"], $row["title"], $row["date"], $row["venue"]) : null;
    }

    /**
     * @param $venue
     * @param null $date
     * @return Event[]
     */
    public static function findByVenueAndDate($venue, $date = null): array
    {
        /*
         * Implementujte statickou metodu findByVenueAndDate s parametrem venue a volitelným parametrem date,
         *  která v databázi najde všechny záznamy s daným místem konání v daný datum a vrátí je jako pole
         * instancí třídy Event.
Pokud není datum date zadán, hledá se pouze podle místa konání.
Pokud žádné vyhovující záznamy neexistují, vrátí prázdné pole.
         */
        $statement = self::$pdo->prepare(
        $date === null ? 'SELECT * FROM events WHERE venue = :venue'
            : 'SELECT * FROM events WHERE venue = :venue AND date = :date'
        );

        if ( !$statement || !$statement->execute(['venue' => $venue, 'date' => $date]) )
            return [];

        $rows = $statement->fetchAll(\PDO::FETCH_ASSOC);
        return array_map( fn ($row) => new Event($row["id"], $row["title"], $row["date"], $row["venue"]), $rows);
    }

}
