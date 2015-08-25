<?php

class Util
{
    public static function isValid($key, &$value, $source = null)
    {
        global $_REQUEST;
        
        if ($source == null)
        {
            $source = $_REQUEST;
        }

        if (is_array($key))
        {
            $retval = array();
            $value = array();

            for ($i = 0; $i < count($key); $i++)
            {
                $retval[$i] = isset($source[$key[$i]]) && !empty($source[$key[$i]]);
                
                if ($retval[$i])
                {
                    $value[$key[$i]] = $source[$key[$i]];
                }
            }
            
            return !in_array(false, $retval);
        }
        else
        {
            $retval = isset($source[$key]) && !empty($source[$key]);

            if ($retval)
            {
                $value = $source[$key];
            }

            return $retval;
        }
    }

    public static function encode($text)
    {
        return htmlentities($text);
    }

    public static function timeToString($datetime, $format = 'd.m.Y H:i')
    {
        return date($format, strtotime($datetime));
    }

    public static function in_iarray($str, $arr)
    {
        foreach($arr as $value)
        {
            if (strcasecmp($str, $value) == 0)
            {
                return true;
            }
        }

        return false;
    }

    public static function array_iunique($arr)
    {
        $narray = array();

        foreach($arr as $key => $value)
        {
            if (!Util::in_iarray($value, $narray))
            {
                $narray[$key] = $value;
            }
        }

        return $narray;
    }
}

?>
