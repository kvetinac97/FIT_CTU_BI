<?php

namespace App\Controller;

use App\Entity\Building;
use App\Entity\Room;
use App\Form\BuildingType;
use App\Service\EntityService\BuildingService;
use App\Service\RelationService\BuildingRelationService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("/building")
 */
class BuildingController extends AbstractController
{
    private BuildingService $service;
    private BuildingRelationService $relationService;

    public function __construct(BuildingService $buildingService,
                                BuildingRelationService $buildingRelationService)
    {
        $this->service = $buildingService;
        $this->relationService = $buildingRelationService;
    }

    /**
     * @Route("/", name="building_index", methods={"GET"})
     */
    public function index(Request $request): Response
    {
        $page = $this->service->getPage($request->query);

        return $this->render('building/index.html.twig', [
            'buildings' => $page->getData(),
            'current' => $page->getPage(),
            'pages' => $page->getPages(),
            'params' => $page->getUrlParams(),
            'menu' => 'building'
        ]);
    }

    /**
     * @Route("/new", name="building_new", methods={"GET", "POST"})
     */
    public function new(Request $request): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $building = new Building();
        $form = $this->createForm(BuildingType::class, $building);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->create($building);

            return $this->redirectToRoute('building_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('building/new.html.twig', [
            'building' => $building,
            'form' => $form,
            'menu' => 'building'
        ]);
    }

    /**
     * @Route("/{id}", name="building_show", methods={"GET"})
     */
    public function show(Building $building): Response
    {
        return $this->render('building/show.html.twig', [
            'building' => $building,
            'menu' => 'building',
        ]);
    }


    /**
     * @Route("/{id}/rooms", name="building_rooms", methods={"GET"})
     */
    public function rooms(Building $building, Request $request): Response
    {
        if (!$this->isGranted('ROLE_USER'))
            $request->query->set('public', true);

        $page = $this->relationService->getRoomsPage($building, $request->query);

        return $this->render('building/rooms.html.twig', [
            'building' => $building,
            'rooms' => $page->getData(),
            'pages' => $page->getPages(),
            'current' => $page->getPage(),
            'params' => $page->getUrlParams(),
            'menu' => 'building'
        ]);
    }

    /**
     * @Route("/{id}/edit", name="building_edit", methods={"GET", "POST"})
     */
    public function edit(Request $request, Building $building): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        $form = $this->createForm(BuildingType::class, $building);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->service->edit($building);

            return $this->redirectToRoute('building_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('building/edit.html.twig', [
            'building' => $building,
            'form' => $form,
            'menu' => 'building',
        ]);
    }

    /**
     * @Route("/{id}", name="building_delete", methods={"POST"})
     */
    public function delete(Request $request, Building $building): Response
    {
        $this->denyAccessUnlessGranted('ROLE_ADMIN');
        if ($this->isCsrfTokenValid('delete'.$building->getId(), $request->request->get('_token'))) {
            $this->service->remove($building);
        }

        return $this->redirectToRoute('building_index', [], Response::HTTP_SEE_OTHER);
    }
}
