<?php

namespace App\Service\AbstractService\PageService;


use Doctrine\ORM\Tools\Pagination\Paginator;

class Page
{
    private int $size;   // current page size
    private int $page;   // current page
    private int $pages;  // total pages

    private Paginator $data;
    private array $criteria;
    private array $order;

    public function __construct(int $page,
                                int $size,
                                int $pages,
                                Paginator $data,
                                array $criteria = [],
                                array $order = [])
    {
        $this->data = $data;
        $this->criteria = $criteria;
        $this->order = $order;
        $this->page = $page;
        $this->size = $size;
        $this->pages = $pages;
    }

    /**
     * @return Paginator
     */
    public function getData(): Paginator
    {
        return $this->data;
    }

    /**
     * @return array
     */
    public function getCriteria(): array
    {
        return $this->criteria;
    }


    /**
     * @return int
     */
    public function getPage(): int
    {
        return $this->page;
    }

    /**
     * @return int
     */
    public function getSize(): int
    {
        return $this->size;
    }

    /**
     * @return int
     */
    public function getPages(): int
    {
        return $this->pages;
    }


    /**
     * @return array
     */
    public function getOrder(): array
    {
        return [ $this->order['by'] , $this->order['dir'] ];
    }

    /**
     * @return array
     */
    public function getUrlParams() : array{
        $params = $this->getCriteria();
        if(!empty($this->order)) {
            $params += $this->order;
        }

        return $params;
    }

//    public function getUrlParamsOppositeOrder(): array
//    {
//        $urlParamsOrder = $this->getUrlParams();
//
//        $orderBy =  $this->order['orderBy'];
//        $urlParamsOrder['orderBy'] = $orderBy; // === "" ? 'name' : $orderBy;
//        $urlParamsOrder['orderDir'] = $this->getOppositeDir($this->order['orderDir']);
//        return $urlParamsOrder;
//    }

//    public static function getOppositeDir($dir): string{
//        return $dir === 'ASC' ? 'DESC' : 'ASC';
//    }
}
