<?php

class Map extends Controller
{
    private $params;

    private $width;
    private $height;
    private $lat;
    private $lon;
    private $zoom;

    public function __construct($params)
    {
        $this->params = $params;
    }

    /**
     * Is an action method fo the controller
     * which produces a map using specific
     * values for width, height, physical
     * coordinates and zoom level.
     */
    public function getMap()
    {
        REST::requireRequestMethod('GET');

        $requiredParameters = array('width', 'height', 'lat', 'lon', 'zoom');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            /**
             * Failure tolerance
             */
            $this->width = $width > 512 ? 512 : ($width < 0 ? 0 : $width);
            $this->height = $height > 512 ? 512 : ($height < 0 ? 0 : $height);
            $this->lat = (double)$lat;
            $this->lon = (double)$lon;
            $this->zoom = $zoom > 18 ? 18 : ($zoom < 0 ? 0 : $zoom);

            Util::isValid('center', $center, $this->params);
            $image = $this->generateMap(isset($center) ? true : false);

            REST::sendResponse(200, $image, 'image/png');
        }
        else
        {
            REST::sendResponse(400);
        }
    }

    public function translateCoordinates()
    {
        REST::requireRequestMethod('GET');

        $requiredParameters = array('width', 'height', 'lat', 'lon', 'zoom', 'reflat', 'reflon');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            /**
             * Failure tolerance
             */
            $this->width = $width > 512 ? 512 : ($width < 0 ? 0 : $width);
            $this->height = $height > 512 ? 512 : ($height < 0 ? 0 : $height);
            $this->lat = (double)$lat;
            $this->lon = (double)$lon;
            $this->zoom = $zoom > 18 ? 18 : ($zoom < 0 ? 0 : $zoom);

            list($x, $y) = $this->translate($reflat, $reflon);

            $translatedCoordinatesNode = new XMLNode('translatedcoordinates');
            $translatedCoordinatesNode->setAttributes(array('x' => $x, 'y' => $y));

            $mapNode = new XMLNode('map');
            $mapNode->setAttributes($values);
            $mapNode->addNode($translatedCoordinatesNode);

            $xmlDoc = new XMLDocument();
            $xmlDoc->setRootNode($mapNode);

            REST::sendResponse(200, $xmlDoc, 'application/xml');
        }
        else
        {
            REST::sendResponse(400);
        }
    }

    public function getTileUrl()
    {
        REST::requireRequestMethod('GET');

        $requiredParameters = array('lat', 'lon', 'zoom');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            $url = "http://tile.openstreetmap.org/" . Map::getTileNumber($lat, $lon, $zoom) . ".png";

            $urlNode = new XMLNode('url');
            $urlNode->setAttribute('href', $url);

            $mapNode = new XMLNode('map');
            $mapNode->setAttributes($values);
            $mapNode->addNode($urlNode);

            $xmlDoc = new XMLDocument();
            $xmlDoc->setRootNode($mapNode);

            REST::sendResponse(200, $xmlDoc, 'application/xml');
        }
        else
        {
            REST::sendResponse(400);
        }
    }

    private function translate($lat, $lon)
    {
        list($xTile, $yTile) = Map::getTileNumbers($this->lat, $this->lon, $this->zoom);
        list($x, $y) = BoundingBox::getTileCoordinates($xTile, $yTile, $this->zoom, $lat, $lon);

        if ($this->width > 256 || $this->height > 256)
        {
            list($xTile, $yTile) = Map::getTileNumbers($this->lat, $this->lon, $this->zoom);

            return array($x + 256 * Map::heavidise($xTile - $transX), 255 - ($y + 256 * Map::heavidise($yTile - $transY)));
        }

        return array($x, $y);
    }

    /**
     * Generates a map using specific coordinates
     * and a specific zoom level for a given map
     * size. Optionally the specified coordinates
     * can be centered.
     */
    private function generateMap($center = false)
    {
        $cache = false;

        if ($cache && Cache::exists($this->getHash($center)))
        {
            return Cache::get($this->getHash($center));
        }

        list($xTile, $yTile) = Map::getTileNumbers($this->lat, $this->lon, $this->zoom);
        list($x, $y) = BoundingBox::getTileCoordinates($xTile, $yTile, $this->zoom, $this->lat, $this->lon);

        if ($this->width > 256 || $this->height > 256)
        {
            $padLeft = $x < 128;
            $padTop = $y < 128;

            if ($padLeft && $padTop)
            {
                $topLeft = Map::getMapTileXY($xTile - 1, $yTile - 1, $this->zoom);
                $topRight = Map::getMapTileXY($xTile, $yTile - 1, $this->zoom);
                $bottomLeft = Map::getMapTileXY($xTile - 1, $yTile, $this->zoom);
                $bottomRight = Map::getMapTileXY($xTile, $yTile, $this->zoom);
            }
            else if ($padLeft && !$padTop)
            {
                $topLeft = Map::getMapTileXY($xTile - 1, $yTile, $this->zoom);
                $topRight = Map::getMapTileXY($xTile, $yTile, $this->zoom);
                $bottomLeft = Map::getMapTileXY($xTile - 1, $yTile + 1, $this->zoom);
                $bottomRight = Map::getMapTileXY($xTile, $yTile + 1, $this->zoom);
            }
            else if (!$padLeft && !$padTop)
            {
                $topLeft = Map::getMapTileXY($xTile, $yTile, $this->zoom);
                $topRight = Map::getMapTileXY($xTile + 1, $yTile, $this->zoom);
                $bottomLeft = Map::getMapTileXY($xTile, $yTile + 1, $this->zoom);
                $bottomRight = Map::getMapTileXY($xTile + 1, $yTile + 1, $this->zoom);
            }
            else if (!$padLeft && $padTop)
            {
                $topLeft = Map::getMapTileXY($xTile, $yTile - 1, $this->zoom);
                $topRight = Map::getMapTileXY($xTile + 1, $yTile - 1, $this->zoom);
                $bottomLeft = Map::getMapTileXY($xTile, $yTile, $this->zoom);
                $bottomRight = Map::getMapTileXY($xTile + 1, $yTile, $this->zoom);
            }

            $img = imagecreatetruecolor(512, 512);

            imagecopy($img, $topLeft, 0, 0, 0, 0, 256, 256);
            imagecopy($img, $topRight, 256, 0, 0, 0, 256, 256);
            imagecopy($img, $bottomLeft, 0, 256, 0, 0, 256, 256);
            imagecopy($img, $bottomRight, 256, 256, 0, 0, 256, 256);

            $output = imagecreatetruecolor($this->width, $this->height);

            if ($center)
            {
                if ($padLeft)
                {
                    $xPos = 256 + $x - $this->width / 2;
                }
                else
                {
                    $xPos = $x - $this->width / 2;
                }

                if ($padTop)
                {
                    $yPos = 256 + $y - $this->height / 2;
                }
                else
                {
                    $yPos = $y - $this->width / 2;
                }

                $xPos = $xPos < 0 ? 0 : $xPos;
                $xPos = $xPos > 512 - $this->width ? 512 - $this->width : $xPos;
                $yPos = $yPos < 0 ? 0 : $yPos;
                $yPos = $yPos > 512 - $this->height ? 512 - $this->height : $yPos;

                imagecopy($output, $img, 0, 0, $xPos, $yPos, $this->width, $this->height);
            }
            else
            {
                $xPos = $padLeft ? 0 : 256;
                $yPos = $padTop ? 0 : 256;
                $xPos = $xPos + $this->width > 512 ? 512 - $this->width : $xPos;
                $yPos = $yPos + $this->height > 512 ? 512 - $this->height : $yPos;

                imagecopy($output, $img, 0, 0, $xPos, $yPos, $this->width, $this->height);
            }
        }
        else
        {
            $output = imagecreatetruecolor($this->width, $this->height);
            $img = Map::getMapTile($this->lat, $this->lon, $this->zoom);

            if ($center)
            {
                $xPos = $x - $this->width / 2;
                $yPos = $y - $this->width / 2;
                $xPos = $xPos < 0 ? 0 : $xPos;
                $yPos = $yPos < 0 ? 0 : $yPos;
            }
            else
            {
                $xPos = $x > $this->width ? 256 - $this->width : 0;
                $yPos = $y > $this->height ? 256 - $this->height : 0;
            }

            imagecopy($output, $img, 0, 0, $xPos, $yPos, $this->width, $this->height);
        }

        if (!Cache::exists($this->getHash($center)))
        {
            Cache::init($this->getHash($center), $output);
        }

        return $output;
    }

    /**
     * Generates a hash used for caching.
     */
    private function getHash($center)
    {
        return md5($this->width . '/' . $this->height . '/' . $this->lat . '/' . $this->lon . '/' . $this->zoom . '/' . ($center ? '1' : '0'));
    }

    /**
     * Calculates the result of the heavidise
     * function which returns 1 if the parameter
     * is greater than zero or 0 otherwise.
     */
    private static function heavidise($x)
    {
        if ($x > 0)
        {
            return 1;
        }

        return 0;
    }

    /**
     * Deprectated method which has been used for
     * debug purposes in the past.
     */
    public static function debugtile($lat, $lon, $zoom)
    {
        $img = Map::getMapTile($lat, $lon, $zoom);
        $red = imagecolorallocate($img, 255, 0, 0);

        list($xTile, $yTile) = Map::getTileNumbers($lat, $lon, $zoom);
        list($x, $y) = BoundingBox::getTileCoordinates($xTile, $yTile, $zoom, $lat, $lon);

        $x = floor($x);
        $y = floor($y);

        imagerectangle($img, $x - 5, $y - 5, $x + 5, $y + 5, $red);
        header("Content-Type: image/png");
        imagepng($img);
    }

    /**
     * Calculates tile numbers based on the OSM grid system
     * for given physical coordinates and a given zoom level.
     */
    public static function getTileNumbers($lat, $lon, $zoom)
    {
        $xTile = floor((($lon + 180) / 360) * pow(2, $zoom));
        $yTile = floor((1 - log(tan($lat * pi()/180) + 1 / cos($lat* pi()/180)) / pi()) /2 * pow(2, $zoom));

        return array($xTile, $yTile);
    }

    /**
     * Generates a string used by OSM to identify map tiles
     * within the directory structure of tile servers.
     */
    public static function getTileNumber($lat, $lon, $zoom)
    {
        $tilePositions = Map::getTileNumbers($lat, $lon, $zoom);
            return $zoom . "/" . $tilePositions[0] . "/" . $tilePositions[1];
    }

    /**
     * Creates an image resource for a tile using the
     * physical coordinates and the zoom level by
     * directly downloading it from OSM.
     */
    public static function getMapTile($lat, $lon, $zoom)
    {
        $url = "http://tile.openstreetmap.org/" . Map::getTileNumber($lat, $lon, $zoom) . ".png";

        return imagecreatefrompng($url);
    }

    /**
     * Creates an image resource for a specified tile
     * by directly downloading it from OSM.
     */
    public static function getMapTileXY($xTile, $yTile, $zoom)
    {
        $url = "http://tile.openstreetmap.org/" . $zoom . "/" . $xTile . "/" . $yTile . ".png";

        return imagecreatefrompng($url);
    }
}

?>
