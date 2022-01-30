<?php

namespace App\Service\FileService;

use Symfony\Component\HttpFoundation\File\UploadedFile;

class ImageService
{
    private string $folder;
    private string $defaultImg;

    public function __construct($folder, $defaultImg)
    {
        $this->folder = $folder;
        $this->defaultImg = $defaultImg;
    }

    public function upload(?UploadedFile $image): string
    {
        if($image === null){
            return $this->defaultImg;
        }

        $name = uniqid() . '.' . $image->guessExtension();
        $image->move(
            $this->folder,
            $name
        );

        return $name;
    }

    public function remove($imageName){
        if ($imageName && $imageName !== $this->defaultImg){
            unlink($this->folder . '/' . $imageName);
        }
    }
}