<?php

namespace kvetinac97;

use GdImage;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

class ResizeCommand extends Command {

    protected static string $defaultName = "photoshop:resize";

    protected function configure() {
        // Description
        $this->setDescription("Resize image")
            ->setHelp("Resize image to given size.");

        // Arguments
        $this->addArgument('width', InputArgument::REQUIRED, "Output width")
            ->addArgument('height', InputArgument::REQUIRED, "Output height")
            ->addArgument('input', InputArgument::REQUIRED, "Input path");
    }

    protected function execute (InputInterface $input, OutputInterface $output) : int {
        // Get arguments
        $width = $input->getArgument("width");
        $height  = $input->getArgument("height");
        $inputFile = $input->getArgument("input");

        // Test file
        if ( !is_file($inputFile) ) {
            $output->writeln("Invalid input file.");
            return self::FAILURE;
        }

        // Test width + height
        if (!is_numeric($width) || !is_numeric($height) || $width <= 0 || $height <= 0) {
            $output->writeln("Invalid width or/and height.");
            return self::FAILURE;
        }

        // Create output file
        $inputParts = pathinfo($inputFile);
        $outputFile = $inputParts['dirname'] . DIRECTORY_SEPARATOR .
            $inputParts['filename'] . "-{$width}x{$height}." .
            $inputParts['extension'];

        // Create resource
        $inputGD = match ($inputParts['extension']) {
            'jpeg', 'jpg' => imagecreatefromjpeg($inputFile),
            'png' => imagecreatefrompng($inputFile),
            default => null
        };

        if (!($inputGD instanceof GdImage)) {
            $output->writeln("Invalid input image type");
            return self::FAILURE;
        }

        // Resize
        [$originalWidth, $originalHeight] = getimagesize($inputFile);
        $outputGD = imagecreatetruecolor((int) $width, (int) $height);

        if ( !imagecopyresized($outputGD, $inputGD, 0, 0, 0, 0,
            (int) $width, (int) $height, $originalWidth, $originalHeight) ) {
            $output->writeln("Resizing failed.");
            return self::FAILURE;
        }

        match ($inputParts['extension']) {
            'jpeg', 'jpg' => imagejpeg($outputGD, $outputFile),
            'png' => imagepng($outputGD, $outputFile)
        };

        imagedestroy($outputGD);
        return self::SUCCESS;
    }

}