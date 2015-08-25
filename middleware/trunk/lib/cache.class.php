<?php

class Cache
{
    public static function exists($file)
    {
        return file_exists(CACHE_DIR . $file . CACHE_EXT);
    }

    public static function get($file)
    {
        return imagecreatefrompng(CACHE_DIR . $file . CACHE_EXT);
    }

    public static function init($hash, $handle)
    {
        imagepng($handle, CACHE_DIR . $hash . CACHE_EXT);
    }
}

?>
