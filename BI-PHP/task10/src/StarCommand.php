<?php

namespace Star;

use Symfony\Component\Console\Command\Command;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;

class StarCommand extends Command
{
    protected static $defaultName = 'star';
    protected const MIN_WIDTH = 100;

    protected function configure() {
        $this->setDescription("Draw star")
            ->setHelp("Draw star based on given arguments");

        $this->addArgument('width', InputArgument::REQUIRED, "Canvas width")
            ->addArgument('color', InputArgument::REQUIRED, "Star color")
            ->addArgument('points', InputArgument::REQUIRED, "Star tip count")
            ->addArgument('radius', InputArgument::REQUIRED, "Star tip radius")
            ->addArgument('output', InputArgument::REQUIRED, "Output file name")
            ->addArgument('bgColor', InputArgument::OPTIONAL, "Background color (def white)")
            ->addArgument('borderColor', InputArgument::OPTIONAL, "Border color")
            ->addArgument('borderWidth', InputArgument::OPTIONAL, "Border width (in px)");
    }

    /**
     * @param $image
     * @param string $color
     * @return int|false
     */
    private function fromRgbString ( $image, string $color ) {
        // Validate pattern
        if ( !preg_match("/^[a-fA-F0-9]{6}$/", $color) )
            return false;

        // Create color
        $red = substr($color, 0, 2);
        $green = substr($color, 2, 2);
        $blue = substr($color, 4, 2);
        return imagecolorallocate($image, hexdec($red), hexdec($green), hexdec($blue));
    }

    protected function execute (InputInterface $input, OutputInterface $output) : int {
        // Init arguments
        $width = $input->getArgument("width");
        $tips = $input->getArgument("points");
        $radius = $input->getArgument("radius");
        $outputName = $input->getArgument("output");
        $outputFile = pathinfo($outputName, PATHINFO_EXTENSION) == 'png' ? $outputName : $outputName . '.png';

        $color = $input->getArgument("color");
        $bgColor = $input->getArgument("bgColor") ?? "FFFFFF";
        $borderColor = $input->getArgument("borderColor"); $borderInt = null;
        $borderWidth = $input->getArgument("borderWidth");

        // Validate numeric arguments
        if ( !is_numeric($width) || $width < self::MIN_WIDTH ) {
            $output->writeln(sprintf("Error: Width must be greater than %s.", self::MIN_WIDTH));
            return self::FAILURE;
        }
        if ( !is_numeric($tips) || $tips < 3 ) {
            $output->writeln("Error: Number of points must be integer greater than 3.");
            return self::FAILURE;
        }
        if ( !is_numeric($radius) || $radius < 0.1 || $radius > 0.9 ) {
            $output->writeln("Error: Radius must be float between 0.1 and 0.9.");
            return self::FAILURE;
        }
        if ( $borderWidth !== null && ( !is_numeric($borderWidth) || $borderWidth <= 0 ) ) {
            $output->writeln("Error: Border width must be at least 1.");
            return self::FAILURE;
        }

        // Create image width background
        $img = imagecreatetruecolor($width, $width);
        if (!$img) {
            $output->writeln("Error: Couldn't create image.");
            return self::FAILURE;
        }

        // Validate color arguments
        if ( ($colorInt = $this->fromRgbString($img, $color)) === false ) {
            $output->writeln("Error: Couldn't parse image color.");
            return self::FAILURE;
        }
        if ( ($backgroundInt = $this->fromRgbString($img, $bgColor)) === false ) {
            $output->writeln("Error: Couldn't parse image background color.");
            return self::FAILURE;
        }
        if ( $borderColor !== null && ( ($borderInt = $this->fromRgbString($img, $borderColor)) === false ) ) {
            $output->writeln("Error: Couldn't parse image border color.");
            return self::FAILURE;
        }

        // Prepare points to draw
        $sides = $tips * 2; $x = $width / 2; $start = $width/3;
        $points = []; $points2 = []; $even = true;

        for ( $a = 0; $a <= 360; $a += 360 / $sides ) {
            $even = !$even;

            $points[] = $x + ($start * ($even ? $radius : 1)) * cos(deg2rad($a));
            $points[] = $x + ($start * ($even ? $radius : 1)) * sin(deg2rad($a));

            $points2[] = $x + (($start + $borderWidth) * ($even ? $radius : 1)) * cos(deg2rad($a));
            $points2[] = $x + (($start + $borderWidth) * ($even ? $radius : 1)) * sin(deg2rad($a));
        }

        // Perform image operations
        if ( !imagefill($img, 0, 0, $backgroundInt) ) {
            $output->writeln("Error: Couldn't draw image background.");
            return self::FAILURE;
        }
        if ( $borderInt !== null && !imagefilledpolygon($img, $points2, $sides, $borderInt) ) {
            $output->writeln("Error: Couldn't draw star border.");
            return self::FAILURE;
        }
        if ( !imagefilledpolygon($img, $points, $sides, $colorInt) ) {
            $output->writeln("Error: Couldn't draw star.");
            return self::FAILURE;
        }

        // Correct star position for stars with odd number of sides
        if ( $tips % 2 == 1 ) {
            $src = imagerotate($img, 180 / $sides, $backgroundInt);
            $w = imagesx($src); $h = imagesy($src);
            imagecopyresized($img, $src, 0, 0, 0, 0, $width, $width, $w, $h);
        }

        // Check rotation failure
        if ($img === false) {
            $output->writeln("Error: Couldn't draw star");
            return self::FAILURE;
        }

        // Try to save image

        if ( !imagepng($img, $outputFile) ) {
            $output->writeln(sprintf("Error: Couldn't save star to file %s", $outputFile));
            return self::FAILURE;
        }

        $output->writeln(sprintf("Success: star drawn and saved to file %s", $outputFile));
        return self::SUCCESS;
    }

}
