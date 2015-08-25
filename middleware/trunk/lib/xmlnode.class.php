<?php

class XMLNode
{
    private $name;
    private $content;
    private $childNodes;
    private $attributes;
    private $level;
    
    public function __construct($name)
    {
        $this->name = $name;
        $this->content = null;
        $this->childNodes = array();
        $this->attributes = array();
        $this->level = 0;
    }
    
    public function setContent($content)
    {
        $this->content = $content;
    }

    public function addNode($node)
    {
        if ($node instanceof XMLNode)
        {
            $this->childNodes[] = $node;
        }
    }
    
    public function setAttribute($attribute, $value)
    {
        $this->attributes[$attribute] = $value;
    }
    
    public function setAttributes($attributes)
    {
        $this->attributes = array_merge($this->attributes, $attributes);
    }
    
    protected function setLevel($level)
    {
        $this->level = $level;
    }
    
    private function getIndentation()
    {
        return str_pad('', 4 * $this->level);
    }
    
    public function __toString()
    {
        $output = $this->getIndentation() . '<' . $this->name;

        if (count($this->attributes) > 0)
        {
            $output .= ' ';
            
            $attributes = array();
            
            foreach ($this->attributes as $attribute => $value)
            {
                $attributes[] = $attribute . '="' . addslashes($value) . '"';
            }
            
            $output .= implode(' ', $attributes);
        }

        /**
         * Content overwrites child nodes.
         */
        if ($this->content != null)
        {
            $output .= '>' . $this->content . '</' . $this->name . '>' . PHP_EOL;
        }
        else if (count($this->childNodes) > 0)
        {
            $output .= '>' . PHP_EOL;
            
            foreach ($this->childNodes as $childNode)
            {
                $childNode->setLevel($this->level + 1);
                $output .= strval($childNode);
            }
            
            $output .= $this->getIndentation() . '</' . $this->name . '>' . PHP_EOL;
        }
        else
        {
            $output .= ' />' . PHP_EOL;
        }

        return $output;
    }
}

?>