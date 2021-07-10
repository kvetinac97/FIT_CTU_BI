<?php

namespace App;

class DatabaseClass {

    public function dbFunction () : bool {
        // ... some inner database logic not available in test environment
        return true;
    }

}