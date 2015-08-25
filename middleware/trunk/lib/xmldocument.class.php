<?php

class XMLDocument
{
    private $rootNode;
    private $encoding;

    public function __construct()
    {
        $this->rootNode = null;
        $this->encoding = 'UTF-8';
    }

    public function setRootNode($rootNode)
    {
        $this->rootNode = $rootNode;
    }

    public function setEncoding($encoding)
    {
        $this->encoding = $encoding;
    }

    public function __toString()
    {
        $output = '<?xml version="1.0" encoding="' . $this->encoding . '" standalone="yes"?>' . PHP_EOL;

        if ($this->rootNode != null)
        {
            $output .= strval($this->rootNode);
        }

        return $output;
    }
}

?>