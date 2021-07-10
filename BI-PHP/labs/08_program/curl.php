<?php

// Fungujeme jako klient
$res = curl_init("https://ackeeedu.000webhostapp.com/api.php/records/posts/1");
curl_setopt($res, CURLOPT_CUSTOMREQUEST, 'GET');
curl_setopt($res, CURLOPT_RETURNTRANSFER, true);
var_dump(curl_exec($res));
var_dump(curl_getinfo($res));
curl_close($res);