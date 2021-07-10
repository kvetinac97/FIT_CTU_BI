<?php


namespace HW\Lib;


class UserService
{
    private $storage;

    public function __construct(Storage $storage)
    {
        $this->storage = $storage;
    }

    public function createUser($username, $email)
    {
        $id = uniqid();
        $this->storage->save($id, json_encode([
            'username' => $username,
            'email' => $email
        ]));
        return $id;
    }

    public function getUsername($id)
    {
        $user = $this->storage->get($id);
        if (!$user) {
            return null;
        }
        return json_decode($user, true)['username'];
    }

    public function getEmail($id)
    {
        $user = $this->storage->get($id);
        if (!$user) {
            return null;
        }
        return json_decode($user, true)['email'];
    }
}