<?php

namespace App\Controller\Rest;

use App\Entity\Building;
use App\Form\BuildingType;
use App\Service\EntityService\BuildingService;
use Doctrine\DBAL\Exception\ConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class BuildingRestController extends RestController
{
    private BuildingService $service;

    public function __construct(BuildingService $buildingService)
    {
        $this->service = $buildingService;
    }

    /**
     * @Rest\Get("/buildings", name="api_building_list")
     */
    public function listAction (Request $request): JsonResponse
    {
        $page = $this->service->getPage($request->query);
        $users = $page->getData();
        $currentPage = $page->getPage();

        $nextPageUrl = null;
        $prevPageUrl = null;
        if ($currentPage > 1){
            $prevPageUrl = $this->generateUrl('api_building_list', $page->getUrlParams() + ["page" => $currentPage - 1]);
        }

        if ($currentPage < $page->getPages()) {
            $nextPageUrl = $this->generateUrl('api_building_list', $page->getUrlParams() + ["page" => $currentPage + 1]);
        }

        $currentPageUrl = $this->generateUrl('api_building_list', $page->getUrlParams() + ["page" => $currentPage]);

        return $this->json(
            [
                "code" => Response::HTTP_OK,
                "page" => $page->getPage(),
                "prev" => $prevPageUrl,
                "next" => $nextPageUrl,
                "current" => $currentPageUrl,
                "pages" => $page->getPages(),
                "params" => ["page" => $currentPage] + $page->getUrlParams(),
                "data" => $users
            ],
            Response::HTTP_OK,
            [],
            ['groups' => ['building']]);
    }

    /**
     * @Rest\Get("/buildings/{id}", name="api_building_detail")
     */
    public function detailAction(string $id): JsonResponse
    {
        if (!($building = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no building with ID $id"
            ], Response::HTTP_NOT_FOUND);

        return $this->json(
            $building,
            Response::HTTP_OK,
            [],
            ['groups' => ['building']]);
    }


    /**
     * @Rest\Post("/buildings", name="api_building_add")
     */
    public function addAction(Request $request): JsonResponse
    {
        $building = new Building();
        $form = $this -> createForm(BuildingType::class, $building, ['csrf_protection' => false]);
        $form -> handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> create($building); }
            catch (ConstraintViolationException $exception) {
                return $this->json(
                    [
                        "code" => Response::HTTP_BAD_REQUEST,
                        "error" => get_class($exception)
                    ],
                    Response::HTTP_BAD_REQUEST
                );
            }

            return $this -> json(
                $building,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['building']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/buildings/{id}", name="api_building_edit")
     */
    public function editAction(Request $request, int $id): JsonResponse
    {
        if ( ($building = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $form = $this->createForm(BuildingType::class, $building, ['csrf_protection' => false])
            ->submit($request->query->all(), false);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> edit($building); }
            catch (ConstraintViolationException $exception) {
                return $this->json(
                    [
                        "code" => Response::HTTP_BAD_REQUEST,
                        "error" => get_class($exception)
                    ],
                    Response::HTTP_BAD_REQUEST
                );
            }

            return $this->json(
                $building,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['building']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST,
                "error" => $form->getErrors()
            ],
            Response::HTTP_BAD_REQUEST);
    }

}

