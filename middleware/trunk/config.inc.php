<?php

define("CACHE_DIR", "cache/");
define("CACHE_EXT", ".png");

define('MYSQL_HOSTNAME', 'localhost');
define('MYSQL_USERNAME', 'zonetrak');
define('MYSQL_PASSWORD', 'zonetrak');
define('MYSQL_DATABASE', 'zonetrak');

set_include_path('controller' . PATH_SEPARATOR . 'lib');

function __autoload($class_name)
{
    require_once strtolower($class_name) . '.class.php';
}

?>