<?php

namespace App\Controller\Rest;

use App\Entity\Member;
use App\Entity\RoomUser;
use App\Service\EntityService\GroupService;
use App\Service\EntityService\MemberService;
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
class GroupMemberRestController extends RestController
{
    private MemberService $service;
    private UserService $userService;
    private GroupService $groupService;

    public function __construct(MemberService $memberService,
                                UserService $userService,
                                GroupService $groupService)
    {
        $this->service = $memberService;
        $this->userService = $userService;
        $this->groupService = $groupService;
    }


    /**
     * @Rest\Delete("/members/{id}", name="api_members_delete")
     */
    public function deleteAction(int $id): JsonResponse
    {
        $groupMember = $this->service->find($id);
        if ( $groupMember === null ){
            return $this->json(
                [],
                Response::HTTP_NOT_FOUND
            );
        }

        $this->service->remove($groupMember);

        return new JsonResponse(
            null,
            Response::HTTP_NO_CONTENT
        );
    }


    /**
     * @Rest\Post("/members", name="api_members_create")
     */
    public function createAction(Request $request): JsonResponse
    {
        $params = json_decode($request->getContent(), true);

        $groupId = $params["group"];
        $group = $this->groupService->find($groupId);
        if ( $group === null ){
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

        if(($groupMember = $this->service->findBy($group, $user)) !== null){
            return new JsonResponse(
                ["data" => $groupMember],
                Response::HTTP_FOUND
            );
        }

        $groupMember = new Member();
        $groupMember->setUser($user);
        $groupMember->setGroup($group);
        $groupMember->setManager($manager);

        $this->service->create($groupMember);

        return new JsonResponse(
            ["data" => $groupMember],
            Response::HTTP_CREATED
        );
    }


    /**
     * @Rest\Patch("/members/{id}", name="api_members_change")
     */
    public function changeRoleAction(Request $request, int $id): JsonResponse
    {
        $groupMember = $this->service->find($id);
        if ( $groupMember === null ){
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

        if ( $manager !== $groupMember->getManager()  ) {
            $groupMember->setManager($manager);
        }


        $this->service->edit();

        return new JsonResponse(
            [
                "data" => $groupMember,
                "code" => Response::HTTP_OK
            ],
            Response::HTTP_OK
        );
    }

}
