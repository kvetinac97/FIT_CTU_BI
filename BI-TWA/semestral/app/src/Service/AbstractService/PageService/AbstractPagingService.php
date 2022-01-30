<?php

namespace App\Service\AbstractService\PageService;


use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\QueryBuilder;
use Symfony\Component\HttpFoundation\InputBag;

abstract class AbstractPagingService
{
    protected ServiceEntityRepository $repository;

    public function __construct(ServiceEntityRepository $repository)
    {
        $this->repository = $repository;
    }


    public function getPage(InputBag $query, PagingQuery $pq = null): Page
    {
        // VALIDATE QUERY
        [$page, $size] = $this->getPageAndSize($query);
        $order = $this->getOrder($query);
        $criteria = $this->getCriteria($query);
        $dateCriteria = $this->getDateCriteria($query);
        $joinCriteria = $this->getJoinCriteria($query);
        $decompositionJoinCriteria = $this->getDecompositionJoinCriteria($query);

        // GET DATA
        $pagingQuery = $pq ?? new PagingQuery($this->repository);
        $data = $pagingQuery
            ->addSearch($criteria, $dateCriteria, $joinCriteria, $decompositionJoinCriteria)
            ->setOrder($order)
            ->setPaging($page, $size)
            ->getResult();

        $pages = ceil($data->count() / $size);

        return new Page($page, $size, $pages, $data, $this->getPageCriteria($query), $order);
    }

    private function getPageAndSize(InputBag $query): array
    {
        $page = max(1, $query->getInt('page', 1));
        $size = min(100, max(1, $query->getInt('size', 10)));

        return [ $page, $size ];
    }

    private function getOrder(InputBag $query): ?array
    {
        $by = $query->get('by');
        $dir = $query->get('dir');

        $orderFields = $this->getOrderFields(); // ['name', 'phone', 'room', 'email'];
        if (empty($by) || !in_array($by, $orderFields)){
            return [];
        }

        if(empty($dir) && $dir !== "ASC" && $dir !== "DESC"){
            $dir = "ASC";
        }

        return ["by" => $by, "dir" => $dir];
    }

    // Override Methods for ordering & filtering
    protected function getCriteria(InputBag $query): array
    { return []; }

    protected function getPageCriteria(InputBag $query): array
    { return $this->getCriteria($query); }

    protected function getOrderFields(): array
    { return []; }

    protected function getDateCriteria(InputBag $query): array
    { return []; }

    protected function getJoinCriteria(InputBag $query): array
    { return []; }

    protected function getDecompositionJoinCriteria(InputBag $query): array
    { return []; }
}