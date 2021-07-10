<?php


namespace HW\Lib;


interface Storage
{
    function save($key, $value);

    function get($key);
}