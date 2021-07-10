<?php

namespace kvetinac97;

use GdImage;
use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

class FilterCommand extends Command {

    protected static string $defaultName = "photoshop:filter";

    protected function configure() {
        // Description
        $this->setDescription("Apply filter")
            ->setHelp("Apply black-and-white filter to image.");

        // Arguments
        $this->addArgument('input', InputArgument::REQUIRED, "Input path");
    }

    protected function execute (InputInterface $input, OutputInterface $output) : int {
        // Get arguments
        $inputFile = $input->getArgument("input");

        // Test file
        if ( !is_file($inputFile) ) {
            $output->writeln("Invalid input file.");
            return self::FAILURE;
        }

        // Create output file
        $inputParts = pathinfo($inputFile);
        $outputFile = $inputParts['dirname'] . DIRECTORY_SEPARATOR .
            $inputParts['filename'] . "-bw." . $inputParts['extension'];

        // Create resource
        $inputGD = match (strtolower($inputParts['extension'])) {
            'jpeg', 'jpg' => imagecreatefromjpeg($inputFile),
            'png' => imagecreatefrompng($inputFile),
            default => null
        };

        if (!($inputGD instanceof GdImage)) {
            $output->writeln("Invalid input image type");
            return self::FAILURE;
        }

        // Resize
        if ( !imagefilter($inputGD, IMG_FILTER_GRAYSCALE) ) {
            $output->writeln("Applying filter failed.");
            return self::FAILURE;
        }

        match (strtolower($inputParts['extension'])) {
            'jpeg', 'jpg' => imagejpeg($inputGD, $outputFile),
            'png' => imagepng($inputGD, $outputFile)
        };

        imagedestroy($inputGD);
        return self::SUCCESS;
    }

}