<?php

namespace App\Controller\Rest;

use App\Entity\Room;
use App\Form\RoomType;
use App\Service\EntityService\RoomService;
use App\Service\EntityService\UserService;
use App\Service\RelationService\RoomRelationService;
use Doctrine\DBAL\Exception\ConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class RoomRestController extends RestController
{
    private RoomService $service;
    private RoomRelationService $relationService;
    private UserService $userService;

    public function __construct(RoomRelationService $roomRelationService,
                                RoomService $roomService,
                                UserService $userService)
    {
        $this->service = $roomService;
        $this->relationService = $roomRelationService;
        $this->userService = $userService;
    }

    /**
     * @Rest\Get("/rooms", name="api_room_list")
     */
    public function listAction (Request $request): JsonResponse
    {
        if (!$this->isGranted('ROLE_USER'))
            $request->query->set('public', true);

        $page = $this->service->getPage($request->query);
        $currentPage = $page->getPage();

        $nextPageUrl = null;
        $prevPageUrl = null;
        if ($currentPage > 1){
            $prevPageUrl = $this->generateUrl('api_room_list', $page->getUrlParams() + ["page" => $currentPage - 1]);
        }

        if ($currentPage < $page->getPages()) {
            $nextPageUrl = $this->generateUrl('api_room_list', $page->getUrlParams() + ["page" => $currentPage + 1]);
        }

        $currentPageUrl = $this->generateUrl('api_room_list', $page->getUrlParams() + ["page" => $currentPage]);

        $data = $page->getData();
        $showTotalReservations = $request->query->getBoolean('show-total-reservations');
        if($showTotalReservations) {
            $data = $this->service->dtoArray($data);
            $data = $this->service->addTotalReservationsProperty($data);
        }

        $showManager = $request->query->getBoolean('show-manager');
        if ($this->isGranted('ROLE_USER') && $showManager){
            if (!$showTotalReservations){
                $data = $this->service->dtoArray($data);
            }
            $data = $this->service->addManagerProperty($data);
        }
        
        return $this->json(
            [
                "code" => Response::HTTP_OK,
                "page" => $page->getPage(),
                "prev" => $prevPageUrl,
                "next" => $nextPageUrl,
                "current" => $currentPageUrl,
                "pages" => $page->getPages(),
                "params" => ["page" => $currentPage] + $page->getUrlParams(),
                "data" => $data
            ],
            Response::HTTP_OK,
            [],
            ['groups' => ['room', 'roomManager']]);
    }

    /**
     * @Rest\Get("/rooms/{id}", name="api_room_detail")
     */
    public function detailAction(string $id): JsonResponse
    {
        if (!($room = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no room with ID $id"
            ], Response::HTTP_NOT_FOUND);

        return $this->json(
            $room,
            Response::HTTP_OK,
            [],
            ['groups' => ['room']]);
    }

    /**
     * @Rest\Get("/rooms/{id}/users", name="api_room_members")
     */
    public function users(Request $request, Room $room): Response
    {
        $members = $request->query->getBoolean('show-members', true);

        $data = [];
        if ($members){
            [$page, $roomUsers] = $this->relationService->getRoomUsersPage($room, $request->query);
            $data = $roomUsers;
            $groups = ['groups' => ['roomMembers']];
        }
        else{
            $page = $this->relationService->getOtherUsersPage($room, $request->query);
            $data = $page->getData();
            $groups = ['groups' => ['user']];
        }

        return $this->json([
            "code" => Response::HTTP_OK,
            "page" => $page->getPage(),
//            "prev" => $prevPageUrl,
//            "next" => $nextPageUrl,
            'pages' => $page->getPages(),
            'data' => $data,
            'params' => $page->getUrlParams(),
            ],
            Response::HTTP_OK,
        [],
        $groups);
    }



    /**
     * @Rest\Post("/rooms", name="api_room_add")
     */
    public function addAction(Request $request): JsonResponse
    {
        $room = (new Room())->setOpen(false);
        $form = $this -> createForm(RoomType::class, $room, ['csrf_protection' => false]);
        $form -> handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $val = $request->request->get('room')['public'] ?? false;
            $room->setPublic($val == "true" || $val == 1);
            try { $this -> service -> create($room); }
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
                $room,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['room']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/rooms/{id}", name="api_room_edit")
     */
    public function editAction(Request $request, int $id): JsonResponse
    {
        if ( ($room = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $params = json_decode($request->getContent(), true);
        $form = $this->createForm(RoomType::class, $room, ['csrf_protection' => false])
            ->submit($params, false);

        if ($form->isSubmitted() && $form->isValid()) {
//            if ($request->query->has('public')) $room->setPublic($request->query->getBoolean('public'));
            try { $this->service -> edit($room); }
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
                $room,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['room']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST,
                "error" => $form->getErrors()
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Patch("/rooms/{id}", name="api_room_patch")
     */
    public function patchAction(Request $request, int $id): JsonResponse
    {
        if ( ($room = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        if ( !$request->query->has('open') )
            return $this->json(
                [
                    "code" => Response::HTTP_BAD_REQUEST
                ],
                Response::HTTP_BAD_REQUEST
            );

        $room->setOpen($request->query->getBoolean('open'));
        $this->service -> edit($room);

        return $this->json(null, Response::HTTP_NO_CONTENT);
    }

    /**
     * @Rest\Get("/rooms/{id}/access/{userId}", name="api_has_access")
     */
    public function hasAccess(Request $request, int $id, string $userId): JsonResponse
    {
        if ( ($room = $this->service->find($id)) === null || ($user = $this->userService->find($userId)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        return $this->json(
            [
                "access" => $this->service->hasAccess($room, $user)
            ],
            Response::HTTP_OK
        );
    }

}
