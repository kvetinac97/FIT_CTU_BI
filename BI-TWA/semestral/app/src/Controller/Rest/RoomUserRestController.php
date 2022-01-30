<?php

namespace App\Controller\Rest;

use App\Entity\RoomUser;
use App\Service\EntityService\RoomService;
use App\Service\EntityService\RoomUserService;
use App\Service\EntityService\UserService;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class RoomUserRestController extends RestController
{
    private RoomUserService $service;
    private UserService $userService;
    private RoomService $roomService;

    public function __construct(RoomUserService $roomUserService,
                                UserService $userService,
                                RoomService $roomService)
    {
        $this->service = $roomUserService;
        $this->userService = $userService;
        $this->roomService = $roomService;
    }


    /**
     * @Rest\Delete("/roomusers/{id}", name="api_roomusers_delete")
     */
    public function deleteAction(int $id): JsonResponse
    {
        $roomUser = $this->service->find($id);
        if ( $roomUser === null ){
            return $this->json(
                [],
                Response::HTTP_NOT_FOUND
            );
        }

        $this->service->remove($roomUser);

        return new JsonResponse(
            null,
            Response::HTTP_NO_CONTENT
        );
    }


    /**
     * @Rest\Post("/roomusers", name="api_roomusers_create")
     */
    public function createAction(Request $request): JsonResponse
    {
        $params = json_decode($request->getContent(), true);

        $roomId = $params["room"];
        $room = $this->roomService->find($roomId);
        if ( $room === null ){
            return $this->json(
                [],
                Response::HTTP_NOT_FOUND
            );
        }

        $userId = $params["user"];
        $user = $this->userService->find($userId);
        if ( $user === null ){
            return $this->json(
                [],
                Response::HTTP_NOT_FOUND
            );
        }

        $manager = $params["manager"];

        if ( $manager !== false && $manager !== true  ) {
            return $this->json(
                [
                    "code" => Response::HTTP_BAD_REQUEST,
                    "request" => $params
                ],
                Response::HTTP_BAD_REQUEST
            );
        }

        if(($roomUser = $this->service->findBy($room, $user)) !== null){
            return new JsonResponse(
                ["data" => $roomUser],
                Response::HTTP_FOUND
            );
        }

        $roomUser = new RoomUser();
        $roomUser->setUser($user);
        $roomUser->setRoom($room);
        $roomUser->setManager($manager);

        $this->service->create($roomUser);

        return new JsonResponse(
            ["data" => $roomUser],
            Response::HTTP_CREATED
        );
    }


    /**
     * @Rest\Patch("/roomusers/{id}", name="api_roomusers_change")
     */
    public function changeRoleAction(Request $request, int $id): JsonResponse
    {
        $roomUser = $this->service->find($id);
        if ( $roomUser === null ){
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ],
                Response::HTTP_NOT_FOUND
            );
        }

        $params = json_decode($request->getContent(), true);
        $manager = $params["manager"];

        if ( $manager !== false && $manager !== true  ) {
            return $this->json(
                [
                    "code" => Response::HTTP_BAD_REQUEST,
                    "request" => $params
                ],
                Response::HTTP_BAD_REQUEST
            );
        }

        if ( $manager !== $roomUser->getManager()  ) {
            $roomUser->setManager($manager);
        }


        $this->service->edit();

        return new JsonResponse(
            [
                "data" => $roomUser,
                "code" => Response::HTTP_OK
            ],
            Response::HTTP_OK
        );
    }

}
