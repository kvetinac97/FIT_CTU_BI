<?php

namespace App\Service\AbstractService\PageService;

use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\QueryBuilder;
use Doctrine\ORM\Tools\Pagination\Paginator;

class PagingQuery
{
    private QueryBuilder $qb;
    private ServiceEntityRepository $repository;

    public function __construct(ServiceEntityRepository $repository)
    {
        $this->repository = $repository;
        $this->qb = $repository->createQueryBuilder('e');
    }


    public function setPaging($page, $size): self
    {
        if ($this->validatePaging($page, $size)){
            $offset = ($page - 1) * $size;
            $this -> qb
                -> setMaxResults($size)
                -> setFirstResult($offset);
        }

        return $this;
    }

    public function setOrder(array $order = []): self
    {
        if($this->validateOrder($order))
        {
            $this -> qb -> orderBy('e.' . $order["by"], $order['dir']);
        }
        return $this;
    }

    public function addSearch(array $search = [],
                              array $searchDate = [],
                              array $searchJoined = [],
                              array $searchJoinedDecomposition = []): self
    {
        $this->addSimpleSearch($search);
        $this->addDateSearch($searchDate);
        $this->addSearchByOtherEntity($searchJoined);
        $this->addDecompositionSearch($searchJoinedDecomposition);
        return $this;
    }

    public function addSimpleSearch(array $search = [], $like = true): self
    {
        foreach ($search as $searchKey => $searchVal){
            $this -> qb
                -> andWhere("e.$searchKey ".($like ? "LIKE" : "=")." :$searchKey")
                -> setParameter($searchKey, $like ? "%".$searchVal."%" : $searchVal);
        }
        return $this;
    }

    public function orSimpleSearch(array $search = []): self
    {
        foreach ($search as $searchKey => $searchVal){
            $this -> qb
                -> orWhere("e.$searchKey = :$searchKey")
                -> setParameter($searchKey, $searchVal);
        }
        return $this;
    }

    public function addDateSearch(array $searchDate = []): self
    {
        foreach ($searchDate as $searchKey => [$searchVal, $less]){
            $q="e.$searchKey ". ($less ? "<=" : ">=") . " :$searchKey";
                dump($q);
            $this -> qb
                -> andWhere($q)
                -> setParameter($searchKey, $searchVal);
        }
        return $this;
    }

    public function addSearchByOtherEntity(array $searchJoined = []): self
    {
        foreach ($searchJoined as $otherEntity => $id){
            $aliasOther = "other_" . $otherEntity . $id;
            $this -> qb
                -> innerJoin("e.$otherEntity", $aliasOther, 'WITH', "$aliasOther.id = :".$otherEntity."Id")
                -> setParameter($otherEntity."Id", $id);
        }
        return $this;
    }

    public function orSearchByOtherEntity(array $searchJoined = []): self
    {
        foreach ($searchJoined as $otherEntity => $id){
            $this -> qb
                -> leftJoin("e.$otherEntity", 'other_', 'WITH', "other_.id = :".$otherEntity."Id")
                -> setParameter($otherEntity."Id", $id)
                -> orWhere("other_.id IS NOT NULL");
        }
        return $this;
    }

    public function addDecompositionSearch(array $searchJoinedDecomposition = []): self
    {
        foreach ($searchJoinedDecomposition as $otherEntity => [$connector, $id] ){
            $this -> qb
                -> innerJoin("e.$connector", 'conn')
                -> innerJoin("conn.$otherEntity", 'other', 'WITH', "other.id = :".$otherEntity."Id")
                -> setParameter($otherEntity."Id", $id);
        }

        return $this;
    }

    public function getResult(): Paginator
    {
        $query = $this -> qb -> getQuery();
        return new Paginator($query);
    }


    // ----------------- VALIDATORS -----------------
    private function validateOrder(array $order): bool
    {
        if (key_exists('by', $order)
            && key_exists('dir', $order)
            && ($order['dir'] === 'ASC' || $order['dir'] === 'DESC')
        ){
            return true;
        }

        return false;
    }


    private function validatePaging(int $page, int $size): bool
    {
        return $page > 0 && $size > 0;
    }


    public function orSimpleSearchMany(array $search = []) : self
    {
        foreach ($search as $searchKey => $searchValues){
            $this -> qb
                -> orWhere("e.$searchKey IN (:$searchKey)")
                -> setParameter($searchKey, $searchValues);
        }
        return $this;
    }

    public function addSimpleSearchMany(array $search = []) : self
    {
        foreach ($search as $searchKey => $searchValues){
            $this -> qb
                -> andWhere("e.$searchKey IN (:$searchKey)")
                -> setParameter($searchKey, $searchValues);
        }
        return $this;
    }

    public function addNegativeSearchByOtherEntity(array $searchJoined = []): self
    {
        foreach ($searchJoined as $otherEntity => $id){
            $this -> qb
                -> leftJoin("e.$otherEntity", 'other_', 'WITH', "other_.id != :".$otherEntity."Id")
                -> setParameter($otherEntity."Id", $id);
        }
        return $this;
    }


    public function addNegativeDecompositionSearch(array $searchJoinedDecomposition = []): self
    {
        foreach ($searchJoinedDecomposition as $otherEntity => [$connector, $id] ){
            $qbb = $this->repository->createQueryBuilder('eb');

            $notIn = $qbb
                -> select('eb.id')
                -> innerJoin("eb.$connector", 'conn')
                -> innerJoin("conn.$otherEntity", 'other', 'WITH', "other.id = :".$otherEntity."Id");

            $this -> qb
                -> andWhere(
                    $this -> qb
                        -> expr() -> notIn('e.id', $notIn -> getDQL()))
                -> setParameter($otherEntity."Id", $id);
        }

        return $this;
    }

    public function addNegativeSearchByOtherEntityManyToMany(array $searchJoined = []): self
    {
        foreach ($searchJoined as $otherEntity => $id){
            $qbb = $this->repository->createQueryBuilder('eb');

            $notIn = $qbb
                -> select('eb.id')
                -> innerJoin("eb.$otherEntity", 'other_', 'WITH', "other_.id = :".$otherEntity."Id");

            $this -> qb
                -> andWhere(
                    $this -> qb
                        -> expr() -> notIn('e.id', $notIn -> getDQL()))
                -> setParameter($otherEntity."Id", $id);
        }

        return $this;
    }

}