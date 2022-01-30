<?php

namespace App\Controller\Rest;

use App\Entity\Account;
use App\Entity\Reservation;
use App\Form\ReservationType;
use App\Repository\RoomRepository;
use App\Service\EntityService\ReservationService;
use App\Service\EntityService\UserService;
use App\Service\RelationService\ReservationRelationService;
use Doctrine\DBAL\Exception\ConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Controller\Annotations\QueryParam;
use FOS\RestBundle\Request\ParamFetcherInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Validator\Constraints\Json;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class ReservationRestController extends RestController
{
    private ReservationService $service;
    private ReservationRelationService $relationService;

    public function __construct(ReservationService $reservationService,
                                ReservationRelationService $reservationRelationService)
    {
        $this->service = $reservationService;
        $this->relationService = $reservationRelationService;
    }

    /**
     * @Rest\Get("/reservations", name="api_reservation_list")
     */
    public function listAction (Request $request): JsonResponse
    {
        /** @var Account $account */
        $account = $this->getUser();
        $page = ($this->isGranted('ROLE_USER') && !$this->isGranted('ROLE_ADMIN')) ?
            $this->service->getNonAdminReservationsPage($request->query, $account) :
            $this->service->getPage($request->query);
        $reservations = $page->getData();
        $currentPage = $page->getPage();

        $nextPageUrl = null;
        $prevPageUrl = null;
        if ($currentPage > 1){
            $prevPageUrl = $this->generateUrl('api_reservation_list', $page->getUrlParams() + ["page" => $currentPage - 1]);
        }

        if ($currentPage < $page->getPages()) {
            $nextPageUrl = $this->generateUrl('api_reservation_list', $page->getUrlParams() + ["page" => $currentPage + 1]);
        }

        $currentPageUrl = $this->generateUrl('api_reservation_list', $page->getUrlParams() + ["page" => $currentPage]);

        return $this->json(
            [
                "code" => Response::HTTP_OK,
                "page" => $page->getPage(),
                "prev" => $prevPageUrl,
                "next" => $nextPageUrl,
                "current" => $currentPageUrl,
                "pages" => $page->getPages(),
                "params" => ["page" => $currentPage] + $page->getUrlParams(),
                "data" => $reservations
            ],
            Response::HTTP_OK,
            [],
            ['groups' => ['reservation']]);
    }

    /**
     * @Rest\Get("/reservations/{id}", name="api_reservation_detail")
     */
    public function detailAction(string $id): JsonResponse
    {
        if (!($reservation = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no reservation with ID $id"
            ], Response::HTTP_NOT_FOUND);

        return $this->json(
            $reservation,
            Response::HTTP_OK,
            [],
            ['groups' => ['reservation']]);
    }


    /**
     * @Rest\Get("/reservations/{id}/users", name="api_reservation_members")
     */
    public function users(Request $request, int $id): Response
    {
        if (!($reservation = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no reservation with ID $id"
            ], Response::HTTP_NOT_FOUND);


        $members = $request->query->getBoolean('show-members', true);

        if ($members){
            $page = $this->relationService->getUsersPage($reservation, $request->query);
        }
        else{
            $page = $this->relationService->getOtherUsersPage($reservation, $request->query);
        }
        $data = $page->getData();
        $groups = ['groups' => ['user']];

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
     * @Rest\Post("/reservations", name="api_reservation_add")
     */
    public function addAction(Request $request, RoomRepository $roomRepo): JsonResponse
    {
        $reservation = new Reservation();
        $form = $this -> createForm(ReservationType::class, $reservation, [
            'csrf_protection' => false,
            'rest' => true,
            'room_selection' => true,
            'room_ids' => $roomRepo->findAll()
        ]);
        $form -> handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $val = $request->request->get('reservation')['cancelled'] ?? false;
            $reservation->setCancelled($val == "true" || $val == 1);

            $val = $request->request->get('reservation')['approved'] ?? false;
            $reservation->setApproved($val == "true" || $val == 1);
            try { $this -> service -> create($reservation); }
            catch (ConstraintViolationException $exception) {
                return $this->json(
                    [
                        "code" => Response::HTTP_BAD_REQUEST,
                        "error" => ($exception->getMessage())
                    ],
                    Response::HTTP_BAD_REQUEST
                );
            }

            return $this -> json(
                $reservation,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['reservation']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/reservations/{id}", name="api_reservations_edit")
     */
    public function editAction(Request $request, int $id, RoomRepository $roomRepo): JsonResponse
    {
        if ( ($reservation = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $form = $this->createForm(ReservationType::class, $reservation, [
            'csrf_protection' => false,
            'rest' => true,
            'room_selection' => true,
            'room_ids' => $roomRepo->findAll()
        ])->submit($request->query->all(), false);

        if ($form->isSubmitted() && $form->isValid()) {
            if ($request->query->has('approved')) $reservation->setApproved($request->query->getBoolean('approved'));
            if ($request->query->has('cancelled')) $reservation->setCancelled($request->query->getBoolean('cancelled'));
            try { $this->service -> edit($reservation); }
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
                $reservation,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['reservation']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST,
                "error" => $form->getErrors()
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/reservations/{id}/users", name = "api_reservation_users")
     * @QueryParam(name="action")
     * @QueryParam(name="users", requirements=@Json)
     */
    public function actionReservationUsers(string $id, ParamFetcherInterface $fetcher, UserService $userService): Response
    {
        if ( !($reservation = $this->service->find($id)) )
            return $this->handleView($this->view([
                'code' => Response::HTTP_NOT_FOUND,
                'message' => "There is no reservation with ID $id"
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
                foreach ($users as $user) $reservation->addUser($user);
                $this->service->edit($reservation);
                $view = $this->view(null, Response::HTTP_NO_CONTENT);
                break;
            case 'DELETE':
                foreach ($users as $user) $reservation->removeUser($user);
                $this->service->edit($reservation);
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
     * @Rest\Patch("/reservations/{id}", name="api_reservation_patch")
     */
    public function patchAction(Request $request, int $id): JsonResponse
    {
        if ( ($reservation = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        if ( !$request->query->has('approved') )
            return $this->json(
                [
                    "code" => Response::HTTP_BAD_REQUEST
                ],
                Response::HTTP_BAD_REQUEST
            );

        $reservation->setApproved($request->query->getBoolean('approved'));
        $this->service -> edit($reservation);

        return $this->json(null, Response::HTTP_NO_CONTENT);
    }

}

