<?php

require_once 'config.inc.php';

$router = new Router($_SERVER['REQUEST_URI']);

try
{
    $router->connect();
}
catch (Exception $e)
{
    $errorNode = new XMLNode('error');
    $errorNode->setAttributes(array('code' => 400));
    $errorNode->setContent($e->getMessage());

    $xmlDoc = new XMLDocument();
    $xmlDoc->setRootNode($errorNode);
    
    echo strval($xmlDoc);
}

?>