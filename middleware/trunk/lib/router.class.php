<?php

class Router
{
    private $requestUri;
    private $controller;
    private $action;
    private $params;

    public function __construct($requestUri)
    {
        $this->requestUri = $requestUri;
        $this->extractController();
    }

    private function extractController()
    {
        $mvcParts = explode('/', $this->requestUri);
        $this->controller = $mvcParts[1];
        $this->action = $mvcParts[2];
        $this->params = REST::getParameters();
    }

    public function connect()
    {
        $controller = $this->controller;
        $action = $this->action;
        $params = $this->params;

        if (class_exists($controller, true))
        {
            $controllerInstance = new $controller($params);
        }
        else if (empty($controller))
        {
            throw new Exception('The controller has not been specified');
        }
        else
        {
            throw new Exception('There was no controller named ' . $controller);
        }

        if (!($controllerInstance instanceof Controller))
        {
            throw new Exception('The requested class is not a controller');
        }

        if (method_exists($controllerInstance, $action))
        {
            return $controllerInstance->$action();
        }
        else if (empty($action))
        {
            if (method_exists($controllerInstance, 'index'))
            {
                return $controllerInstance->index();
            }

            throw new Exception('The action has not been specified');
        }
        else
        {
            throw new Exception('There was no action named ' . $action);
        }
    }
}

?>