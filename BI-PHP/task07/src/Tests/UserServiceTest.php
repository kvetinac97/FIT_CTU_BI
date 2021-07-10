<?php

namespace HW\Tests;

use HW\Lib\Storage;
use HW\Lib\UserService;
use PHPUnit\Framework\TestCase;

/**
 * @covers \HW\Lib\UserService
 */
class UserServiceTest extends TestCase
{

    private UserService $userService;

    public function setUp(): void {
        parent::setUp();
        $storage = $this->createStub(Storage::class);
        $storage->method('get')->willReturnMap([
            ['nonsense', null],
            ['mock_id', json_encode(["email" => "pine@apple.com", "username" => "pineapple"])]
        ]);
        $this->userService = new UserService($storage);
    }

    public function testBehaviour () {
        $this->userService->createUser('pineapple', 'pine@apple.com');
        $this->assertSame(null, $this->userService->getEmail('nonsense'));
        $this->assertSame('pine@apple.com', $this->userService->getEmail('mock_id'));
        $this->assertSame(null, $this->userService->getUsername('nonsense'));
        $this->assertSame('pineapple', $this->userService->getUsername('mock_id'));
    }

}