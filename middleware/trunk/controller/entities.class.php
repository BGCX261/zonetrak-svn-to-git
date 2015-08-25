<?php

class Entities extends Controller
{
    private $params;

    public function __construct($params)
    {
        $this->params = $params;
    }

    public function updatePosition()
    {
        REST::requireRequestMethod('POST');

        $requiredParameters = array('lat', 'lon', 'friendlyname', 'identifier');

        if (Util::isValid($requiredParameters, $values, $this->params))
        {
            extract($values);

            Model::detachEntity($identifier);

            if (Model::createEntity($identifier, $friendlyname, $lat, $lon))
            {
                REST::sendResponse(201);
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

    public function getRecentEntities()
    {
        REST::requireRequestMethod('GET');

        $db = new MySQL;

        $entitiesNode = new XMLNode('entities');

        foreach (Model::getRecentEntities() as $entity)
        {
            $entityNode = new XMLNode('entity');
            $entityNode->setAttribute('identifier', $entity['IDENTIFIER']);
            $entityNode->setAttribute('friendlyname', $entity['FRIENDLYNAME']);
            $entityNode->setAttribute('lat', $entity['LATITUDE']);
            $entityNode->setAttribute('lon', $entity['LONGITUDE']);
            $entityNode->setAttribute('date', strtotime($entity['DATETIME']));
            $entitiesNode->addNode($entityNode);
        }

        $xmlDoc = new XMLDocument();
        $xmlDoc->setRootNode($entitiesNode);

        REST::sendResponse(200, $xmlDoc, 'application/xml');
    }

    public function getEntities()
    {
        REST::requireRequestMethod('GET');

        $db = new MySQL;

        $entitiesNode = new XMLNode('entities');

        foreach (Model::getEntities() as $entity)
        {
            $entityNode = new XMLNode('entity');
            $entityNode->setAttribute('identifier', $entity['IDENTIFIER']);
            $entityNode->setAttribute('friendlyname', $entity['FRIENDLYNAME']);
            $entityNode->setAttribute('lat', $entity['LATITUDE']);
            $entityNode->setAttribute('lon', $entity['LONGITUDE']);
            $entityNode->setAttribute('date', strtotime($entity['DATETIME']));
            $entitiesNode->addNode($entityNode);
        }

        $xmlDoc = new XMLDocument();
        $xmlDoc->setRootNode($entitiesNode);

        REST::sendResponse(200, $xmlDoc, 'application/xml');
    }
}

?>