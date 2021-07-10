<?php declare(strict_types=1);

namespace FinalTask\Triangle;

class Triangle
{
    // epsilon for float comparison
    const EPSILON = 0.0001;

    /** @var float|int $a */
    private $a;
    /** @var float|int $b */
    private $b;
    /** @var float|int $c */
    private $c;

    /**
     * Triangle constructor.
     * @param float|int $a
     * @param float|int $b
     * @param float|int $c
     * @throws \Exception
     */
    public function __construct($a, $b, $c)
    {
        if ( $a < 0 || $b < 0 || $c < 0 )
            throw new \Exception("Sides of triangle must be positive.");
        if ( $a + $b < $c || $a + $c < $b || $b + $c < $a )
            throw new \Exception("Invalid triangle.");

        $this->a = $a;
        $this->b = $b;
        $this->c = $c;
    }

    /**
     * @return float|int
     */
    public function getA () {
        return $this->a;
    }

    /**
     * @return float|int
     */
    public function getB () {
        return $this->b;
    }

    /**
     * @return float|int
     */
    public function getC () {
        return $this->c;
    }

    /**
     * @return bool
     */
    public function isRight(): bool
    {
        // Determine perpendiculars and hypotenuse
        $max = $this->a; $min1 = $this->b; $min2 = $this->c;
        if ( $this->c > $this->a && $this->c > $this->b ) {
            $max = $this->c;
            $min2 = $this->a;
        }
        if ( $this->b > $this->a && $this->b > $this->c ) {
            $max = $this->b;
            $min1 = $this->a;
            $min2 = $this->c;
        }

        return abs( $min1 * $min1 + $min2 * $min2 - $max * $max ) < self::EPSILON;
    }

    /**
     * @return bool
     */
    public function isEquilateral(): bool
    {
        return abs( $this->a - $this->b ) < self::EPSILON && abs( $this->b - $this->c ) < self::EPSILON
            && abs( $this->c - $this->a ) < self::EPSILON;
    }

    /**
     * @return bool
     */
    public function isIsosceles(): bool
    {
        return abs( $this->a - $this->b ) < self::EPSILON || abs( $this->b - $this->c ) < self::EPSILON
            || abs( $this->c - $this->a ) < self::EPSILON;
    }

    /**
     * @param Triangle $t1
     * @param Triangle $t2
     * @return bool
     */
    public static function isSimilar(Triangle $t1, Triangle $t2): bool
    {
        $sides1 = [$t1->a, $t1->b, $t1->c];
        sort($sides1);
        $sides2 = [$t2->a, $t2->b, $t2->c];
        sort($sides2);

        // Special case, not happening regularly
        if ( $sides2[0] * $sides2[1] * $sides2[2] == 0 )
            return true;

        $proportion = $sides1[0]/$sides2[0];
        return ( $sides1[1] / $sides2[1] - $proportion < self::EPSILON &&
            $sides1[2] / $sides2[2] - $proportion < self::EPSILON );
    }
}
