<?php

use App\MathClass;
use App\DatabaseClass;
use PHPUnit\Framework\TestCase;

/**
 * Class MathTest
 * @covers MathClass
 * @covers DatabaseClass
 */
class MathTest extends TestCase {

    /** @var MathClass */
    private $mathClass;

    /** Called before each test */
    protected function setUp() : void {
        parent::setUp();
        $this->mathClass = new MathClass();
    }

    /** If needed, we can clear after each test */
    protected function tearDown() : void {
        parent::tearDown();
        $this->mathClass = null;
    }

    public function testCreation () {
        $math = new MathClass();
        $this->assertInstanceOf(MathClass::class, $math);
        return $math;
    }

    /**
     * @depends testCreation
     * will pass first parameter, whilst possible
     * depends testSecond would pass second parameter...
     *
     * Passes return value from depends test
     * @param MathClass $math
     */
    public function testAddition ( MathClass $math ) {
        $this->assertTrue($math->add(5, 3) === 8);
        $this->assertTrue($math->add(-3, 3) === 0);
        $this->assertTrue($math->add(-1, -2) === -3);
    }

    /**
     * This test already uses MathClass from setUp()
     * @dataProvider multiProvider
     *
     * @param int $a
     * @param int $b
     * @param int $result
     */
    public function testMultiplication ( int $a, int $b, int $result ) {
        $this->assertEqualsWithDelta($a * $b, $result, 0.0005);
    }

    public function testExceptionSpecific () {
        $this->expectException(\Exception::class);
        new DateTime('fdklfjal');
    }

    public function testExceptionGeneric () {
        $this->expectError();
        print_r( array_map ( fn($x) => $x . " hel", ["test"] ), true );
        include 'hell';
    }

    public function testIsPolite () {
        $this->expectOutputRegex("/He[lr]{2}o!\s/");
        $this->mathClass->sayHi();
    }

    /**
     * Stub vs Mock
     *
     */
    public function testDatabase () {
        $stub = $this->createStub(DatabaseClass::class);
        $stub->method('dbFunction')->willReturn(false, true, true, false);

        $this->assertFalse($stub->dbFunction());
        $this->assertTrue($stub->dbFunction());
        $this->assertTrue($stub->dbFunction());
        $this->assertFalse($stub->dbFunction());

        $mock = $this->createMock(DatabaseClass::class);
        $mock->expects($this->once())->method('dbFunction')->withAnyParameters();
        $mock->dbFunction();
    }

    /* public function multiProvider () {
        return [
            [ 1, 5, 5 ],
            [ 3, 5, 15 ],
            [ -2, 5, -10 ],
            [ 6, -3, -18 ],
            [ -7, -13, 91 ]
        ];
    } if we have less values, se can use array, for more Generator */

    public function multiProvider () : Generator {
        yield [ 15, -3, -45 ];
        yield [ -2, -3, 6 ];
        yield [ 1, 7, 7 ];
        yield [ -6, 7, -42 ];
    }

}