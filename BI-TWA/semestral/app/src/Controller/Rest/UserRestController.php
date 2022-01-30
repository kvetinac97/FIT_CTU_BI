<?php

namespace App\Controller\Rest;

use App\Entity\User;
use App\Form\UserType;
use App\Service\EntityService\UserService;
use Doctrine\DBAL\Exception\ConstraintViolationException;
use FOS\RestBundle\Controller\AbstractFOSRestController as RestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Rest\View(serializerEnableMaxDepthChecks=true)
 */
class UserRestController extends RestController
{
    private UserService $service;

    public function __construct(UserService $userService)
    {
        $this->service = $userService;
    }

    /**
     * @Rest\Get("/users", name="api_user_list")
     */
    public function listAction (Request $request): JsonResponse
    {
        $page = $this->service->getPage($request->query);
        $users = $page->getData();
        $currentPage = $page->getPage();

        $nextPageUrl = null;
        $prevPageUrl = null;
        if ($currentPage > 1){
            $prevPageUrl = $this->generateUrl('api_user_list', $page->getUrlParams() + ["page" => $currentPage - 1]);
        }

        if ($currentPage < $page->getPages()) {
            $nextPageUrl = $this->generateUrl('api_user_list', $page->getUrlParams() + ["page" => $currentPage + 1]);
        }

        $currentPageUrl = $this->generateUrl('api_user_list', $page->getUrlParams() + ["page" => $currentPage]);

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
            ['groups' => ['user']]);
    }

    /**
     * @Rest\Get("/users/{id}", name="api_user_detail")
     */
    public function detailAction(string $id): JsonResponse
    {
        if (!($user = $this->service->find($id)))
            return new JsonResponse([
                "code" => Response::HTTP_NOT_FOUND,
                "message" => "There is no user with ID $id"
            ], Response::HTTP_NOT_FOUND);

        return $this->json(
            $user,
            Response::HTTP_OK,
            [],
            ['groups' => ['user']]);
    }


    /**
     * @Rest\Post("/users", name="api_user_add")
     */
    public function addAction(Request $request): JsonResponse
    {
        $user = new User();
        $form = $this -> createForm(UserType::class, $user, ['csrf_protection' => false]);
        $form -> handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> create($user); }
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
                $user,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['user']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST
            ],
            Response::HTTP_BAD_REQUEST);
    }

    /**
     * @Rest\Put("/users/{id}", name="api_user_edit")
     */
    public function editAction(Request $request, int $id): JsonResponse
    {
        if ( ($user = $this->service->find($id)) === null )
            return $this->json(
                [
                    "code" => Response::HTTP_NOT_FOUND
                ], Response::HTTP_NOT_FOUND
            );

        $form = $this->createForm(UserType::class, $user, ['csrf_protection' => false])
            ->submit($request->query->all(), false);

        if ($form->isSubmitted() && $form->isValid()) {
            try { $this -> service -> edit($user); }
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
                $user,
                Response::HTTP_CREATED,
                [],
                ['groups' => ['user']]);
        }

        return $this->json(
            [
                "code" => Response::HTTP_BAD_REQUEST,
                "error" => $form->getErrors()
            ],
            Response::HTTP_BAD_REQUEST);
    }

}

