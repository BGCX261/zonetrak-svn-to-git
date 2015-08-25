<?php

class Zones extends Controller
{
    private $params;

    public function __construct($params)
    {
        $this->params = $params;
    }

    public function createZone()
    {
        REST::requireRequestMethod('PUT');

        $requiredParameters = array('polygon');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            $polygonData = explode('|', $polygon);

            if (count($polygonData) < 3)
            {
                REST::sendResponse(400);
            }

            $waypoints = array();

            foreach ($polygonData as $polygonDate)
            {
                $waypointData = explode(';', $polygonDate);

                if (count($waypointData) != 2)
                {
                    REST::sendResponse(400);
                }

                $waypoints[] = array('lat' => $waypointData[0], 'lon' => $waypointData[1]);
            }

            $db = new MySQL;

            $zoneID = Model::createZone($type);

            if ($zoneID > 0)
            {
                foreach ($waypoints as $waypoint)
                {
                    if (!Model::createWaypoint($zoneID, $waypoint['lat'], $waypoint['lon']))
                    {
                        REST::sendResponse(500);
                    }
                }

                REST::sendResponse(201, $zoneID);
            }
            else
            {
                REST::sendResponse(500);
            }
        }
        else
        {
            REST::sendResponse(400);
        }
    }

    public function deleteZone()
    {
        REST::requireRequestMethod('DELETE');

        $requiredParameters = array('zoneid');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            if (Model::deleteZone($zoneid))
            {
                REST::sendResponse(200);
            }
            else
            {
                REST::sendResponse(500);
            }
        }
        else
        {
            REST::sendResponse(400);
        }
    }

    public function getZones()
    {
        REST::requireRequestMethod('GET');

        $zonesNode = new XMLNode('zones');

        foreach (Model::getZones() as $zone)
        {
            $zoneNode = new XMLNode('zone');
            $zoneNode->setAttribute('id', $zone['ID']);
            $zoneNode->setAttribute('type', $zone['TYPE']);
            $zoneNode->setAttribute('date', strtotime($zone['CREATED_ON']));
            $zoneNode->setAttribute('deleted', $zone['DELETED_ON'] != '0000-00-00 00:00:00' ? 'true' : 'false');

            $waypointsNode = new XMLNode('waypoints');

            foreach (Model::getWaypoints($zone['ID']) as $waypoint)
            {
                $waypointNode = new XMLNode('waypoint');
                $waypointNode->setAttribute('latitude', $waypoint['LATITUDE']);
                $waypointNode->setAttribute('longitude', $waypoint['LONGITUDE']);
                $waypointsNode->addNode($waypointNode);
            }

            $zoneNode->addNode($waypointsNode);

            $zonesNode->addNode($zoneNode);
        }

        $xmlDoc = new XMLDocument();
        $xmlDoc->setRootNode($zonesNode);

        REST::sendResponse(200, $xmlDoc, 'application/xml');
    }
}

?>