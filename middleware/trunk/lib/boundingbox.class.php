<?php

class BoundingBox
{
    public $north;
    public $south;
    public $west;
    public $east;

    public static function tile2boundingBox($x, $y, $zoom)
    {
        $bb = new BoundingBox;
        $bb->north = BoundingBox::tile2lat($y, $zoom);
        $bb->south = BoundingBox::tile2lat($y + 1, $zoom);
        $bb->west = BoundingBox::tile2lon($x, $zoom);
        $bb->east = BoundingBox::tile2lon($x + 1, $zoom);

        return $bb;
    }

    public static function getTileCoordinates($xTile, $yTile, $zoom, $lat, $lon)
    {
        $bb = BoundingBox::tile2boundingBox($xTile, $yTile, $zoom);
        $minlat = $bb->south;
        $minlon = $bb->west;
        $maxlat = $bb->north;
        $maxlon = $bb->east;
        $deltalat = $maxlat - $minlat;
        $deltalon = $maxlon - $minlon;

        $x = (($lat - $minlat) / $deltalat) * 256;
        $y = 256 - (($lon - $minlon) / $deltalon) * 256;

        return array($x, $y);
    }

    private static function tile2lon($x, $z)
    {
        return ($x / pow(2, $z) * 360) - 180;
    }

    private static function tile2lat($y, $z)
    {
        $n = pi() - ((2.0 * pi() * $y) / pow(2, $z));

        return 180.0 / pi() * atan(0.5 * (exp($n) - exp(-$n)));
    }
}

?>