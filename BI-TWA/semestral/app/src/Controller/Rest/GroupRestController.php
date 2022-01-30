<?php

namespace App\Controller\Rest;


use App\Entity\Account;
use App\Entity\Group;
use App\Form\GroupType;
use App\Service\EntityService\GroupService;
use App\Service\EntityService\RoomService;
use App\Service\EntityService\UserService;
use App\Service\RelationService\GroupRelationService;
use Doctrine\DBAL\Exception\ConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\Annotations\QueryParam;
use FOS\RestBundle\Request\ParamFetcherInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Validator\Constraints\Json;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class GroupRestController extends RestController
{
    private GroupService $service;
    private GroupRelationService $relationService;

    public function __construct(GroupService $groupService,
                                GroupRelationService $groupRelationService)
    {
        $this->service = $groupService;
        $this->relationService = $groupRelationService;
    }

    /**
     * @Rest\Get("/groups", name="api_group_list")
     */
    public function listAction (Request $request): JsonResponse
    {
        /** @var Account $account */
        $account = $this->getUser();
        $page = ($this->isGranted('ROLE_USER') && !$this->isGranted('ROLE_ADMIN')) ?
            $this->service->getNonAdminGroupsPage($request->query, $account) :
            $this->service->getPage($request->query);
        $data = $page->getData();
        $currentPage = $page->getPage();

        $nextPageUrl = null;
        $prevPageUrl = null;
        if ($currentPage > 1){
            $prevPageUrl = $this->generateUrl('api_group_list', $page->getUrlParams() + ["page" => $currentPage - 1]);
        }

        if ($currentPage < $page->getPages()) {
            $nextPageUrl = $this->generateUrl('api_group_list', $page->getUrlParams() + ["page" => $currentPage + 1]);
        }

        $currentPageUrl = $this->generateUrl('api_group_list', $page->getUrlParams() + ["page" => $currentPage]);

        $showManager = $request->query->getBoolean('show-manager');
        if ($showManager){
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
            ['groups' => ['group', 'groupManager']]);
    }

    /**
     * @Rest\Get("/groups/{id}", name="api_group_detail")
     */
    public function detailAction(string $id): JsonResponse
    {
        if (!($group = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no group with ID $id"
            ], Response::HTTP_NOT_FOUND);

        return $this->json(
            $group,
            Response::HTTP_OK,
            [],
            ['groups' => ['group']]);
    }

    /**
     * @Rest\Get("/groups/{id}/users", name="api_group_members")
     */
    public function users(Request $request, Group $group): Response
    {
        $members = $request->query->getBoolean('show-members', true);

        $data = [];
        if ($members){
            [$page, $groupMembers] = $this->relationService->getGroupMembersPage($group, $request->query);
            $data = $groupMembers;
            $groups = ['groups' => ['groupMembers']];
        }
        else{
            $page = $this->relationService->getOtherUsersPage($group, $request->query);
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
     * @Rest\Post("/groups", name="api_group_add")
     */
    public function addAction(Request $request): JsonResponse
    {
        $group = new Group();
        $form = $this -> createForm(GroupType::class, $group, ['csrf_protection' => false]);
        $form -> handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> create($group); }
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
                $group,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['group']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/groups/{id}", name="api_group_edit")
     */
    public function editAction(Request $request, int $id): JsonResponse
    {
        if ( ($group = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $form = $this->createForm(GroupType::class, $group, ['csrf_protection' => false,
                'group_id' => $group->getId()])
            ->submit($request->query->all(), false);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> edit($group); }
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
                $group,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['group']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }


    /**
     * @Rest\Get("/groups/{id}/rooms", name="api_group_rooms")
     */
    public function groupRooms(Request $request, int $id): JsonResponse
    {
        if ( ($group = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $members = $request->query->getBoolean('show-members', true);

        $data = [];
        if ($members){
            [$page, $roomUsers] = $this->relationService->getRoomsPage($group, $request->query);
            $data = $roomUsers;
        }
        else{
            $page = $this->relationService->getOtherRoomsPage($group, $request->query);
            $data = $page->getData();
        }
        $groups = ['groups' => ['room']];

        return $this->json([
            "code" => Response::HTTP_OK,
            "page" => $page->getPage(),
            'pages' => $page->getPages(),
            'data' => $data,
            'params' => $page->getUrlParams(),
        ],
            Response::HTTP_OK,
            [],
            $groups);
    }


    /**
     * @Rest\Put("/groups/{id}/users", name = "api_group_add_user")
     * @QueryParam(name="action")
     * @QueryParam(name="users", requirements=@Json)
     */
    public function actionGroupUsers(string $id, ParamFetcherInterface $fetcher, UserService $userService): Response
    {
        if ( !($group = $this->service->find($id)) )
            return $this->handleView($this->view([
                'code' => Response::HTTP_NOT_FOUND,
                'message' => "There is no group with ID $id"
            ], Response::HTTP_NOT_FOUND));

        $users = array_filter(array_map(fn($userId) => $userService->find($userId),
            json_decode($fetcher->get('users'), true) ?? []));

        if ( empty($users) )
            return $this->handleView($this->view([
                'code' => Response::HTTP_BAD_REQUEST,
                'message' => 'No valid user IDs provided.'
            ], Response::HTTP_BAD_REQUEST));

        switch ($fetcher->get('action') ?? '') {
            case 'ADD':
                $this->service->addUsers($group, $users);
                $view = $this->view(null, Response::HTTP_NO_CONTENT);
                break;
            case 'DELETE':
                $this->service->removeUsers($group, $users);
                $view = $this->view(null, Response::HTTP_NO_CONTENT);
                break;
            default:
                $view = $this->view([
                    'code' => Response::HTTP_BAD_REQUEST,
                    'message' => 'Invalid action, expecting "ADD" or "DELETE".'
                ], Response::HTTP_BAD_REQUEST);
        }

        return $this->handleView($view);
    }



    /**
     * @Rest\Put("/groups/{id}/rooms", name = "api_group_add_room")
     * @QueryParam(name="action")
     * @QueryParam(name="rooms", requirements=@Json)
     */
    public function actionGroupRooms(string $id, ParamFetcherInterface $fetcher, RoomService $roomService): Response
    {
        if ( !($group = $this->service->find($id)) )
            return $this->handleView($this->view([
                'code' => Response::HTTP_NOT_FOUND,
                'message' => "There is no group with ID $id"
            ], Response::HTTP_NOT_FOUND));

        $rooms = array_filter(array_map(fn($roomId) => $roomService->find($roomId),
            json_decode($fetcher->get('rooms'), true) ?? []));

        if ( empty($rooms) )
            return $this->handleView($this->view([
                'code' => Response::HTTP_BAD_REQUEST,
                'message' => 'No valid room IDs provided.'
            ], Response::HTTP_BAD_REQUEST));

        switch ($fetcher->get('action') ?? '') {
            case 'ADD':
                foreach ($rooms as $room) $group->addRoom($room);
                $this->service->edit($group);
                $view = $this->view(null, Response::HTTP_NO_CONTENT);
                break;
            case 'DELETE':
                foreach ($rooms as $room) $group->removeRoom($room);
                $this->service->edit($group);
                $view = $this->view(null, Response::HTTP_NO_CONTENT);
                break;
            default:
                $view = $this->view([
                    'code' => Response::HTTP_BAD_REQUEST,
                    'message' => 'Invalid action, expecting "ADD" or "DELETE".'
                ], Response::HTTP_BAD_REQUEST);
        }

        return $this->handleView($view);
    }

}
