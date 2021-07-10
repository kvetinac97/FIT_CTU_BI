<?php declare(strict_types=1);

namespace App\Model;

use App\Db;

class Account
{
    /** @var integer */
    protected $id;

    /** @var string */
    protected $number;

    /** @var string */
    protected $code;

    /**
     * Account constructor.
     *
     * @param int $id
     * @param string $number
     * @param string $code
     */
    public function __construct(int $id, string $number, string $code)
    {
        $this->id = $id;
        $this->number = $number;
        $this->code = $code;
    }

    /**
     * Creates DB table using CREATE TABLE ...
     */
    public static function createTable(): void
    {
        $db = Db::get();
        $db->query('CREATE TABLE IF NOT EXISTS `account` (
            `id` INTEGER PRIMARY KEY,
            `number` TEXT NOT NULL,
            `code` TEXT NOT NULL
        )');
    }

    /**
     * Drops DB table using DROP TABLE ...
     */
    public static function dropTable(): void
    {
        $db = Db::get();
        $db->query("DROP TABLE IF EXISTS `account`");
    }

    /**
     * Find account record by number and bank code
     *
     * @param string $number
     * @param string $code
     * @return Account|null
     */
    public static function find(string $number, string $code): ?self
    {
        $db = Db::get();
        $selectStatement = $db->prepare("SELECT * FROM `account` WHERE `number` = :number AND `code` = :code");
        $selectStatement->execute(['number' => $number, 'code' => $code]);
        $data = $selectStatement->fetch();
        return empty($data) ? null : new Account(intval($data["id"]), $data["number"], $data["code"]);
    }

    /**
     * Find account record by id
     * 
     * @param int $id
     * @return static|null
     */
    public static function findById(int $id): ?self
    {
        $db = Db::get();
        $selectStatement = $db->prepare("SELECT * FROM `account` WHERE `id` = :id");
        $selectStatement->execute(['id' => $id]);
        $data = $selectStatement->fetch();
        return empty($data) ? null : new Account(intval($data["id"]), $data["number"], $data["code"]);
    }

    /**
     * Inserts new account record and returns its instance; or returns existing account instance
     *
     * @param string $number
     * @param string $code
     * @return static
     */
    public static function findOrCreate(string $number, string $code): self
    {
        if ( ($acc = self::find($number, $code)) !== null )
            return $acc;

        $db = Db::get();
        $insertStatement = $db->prepare("INSERT INTO `account` (`number`, `code`) VALUES (:number, :code)");
        $insertStatement->execute(['number' => $number, 'code' => $code]);
        $data = $db->query("SELECT `id` FROM `account` WHERE ROWID = last_insert_rowid()")->fetch();
        return new Account(intval($data["id"]), $number, $code);
    }

    /**
     * Returns iterable of Transaction instances related to this Account, consider both transaction direction
     *
     * @return iterable<Transaction>
     */
    public function getTransactions(): iterable
    {
        $db = Db::get();
        $selectStatement = $db->prepare("SELECT * FROM `transaction` WHERE `from` = :id OR `to` = :id");
        $selectStatement->execute(['id' => $this->id]);

        $data = $selectStatement->fetchAll();
        return array_map(function($val){
            return new Transaction(
                self::findById(intval($val["from"])),
                self::findById(intval($val["to"])),
                floatval($val["amount"])
            );
        }, $data);
    }

    /**
     * Returns transaction sum (using SQL aggregate function). Treat outgoing transactions as 'minus' and incoming as 'plus'.
     *
     * @return float
     */
    public function getTransactionSum(): float
    {
        $db = Db::get();
        $incomeSt = $db->prepare("SELECT SUM(amount) AS income FROM `transaction` WHERE `to` = :id");
        $incomeSt->execute(['id' => $this->id]);
        $outcomeSt = $db->prepare("SELECT SUM(amount) AS outcome FROM `transaction` WHERE `from` = :id");
        $outcomeSt->execute(['id' => $this->id]);

        $income = floatval($incomeSt->fetch()["income"]);
        $outcome = floatval($outcomeSt->fetch()["outcome"]);
        return $income - $outcome;
    }

    /**
     * @return int
     */
    public function getId(): int
    {
        return $this->id;
    }

    /**
     * @param int $id
     * @return Account
     */
    public function setId(int $id): Account
    {
        $this->id = $id;

        return $this;
    }

    /**
     * @return string
     */
    public function getNumber(): string
    {
        return $this->number;
    }

    /**
     * @param string $number
     * @return Account
     */
    public function setNumber(string $number): Account
    {
        $this->number = $number;

        return $this;
    }

    /**
     * @return string
     */
    public function getCode(): string
    {
        return $this->code;
    }

    /**
     * @param string $code
     * @return Account
     */
    public function setCode(string $code): Account
    {
        $this->code = $code;

        return $this;
    }

    /**
     * Account string representation
     *
     * @return string
     */
    public function __toString()
    {
        return "{$this->number}/{$this->code}";
    }
}
