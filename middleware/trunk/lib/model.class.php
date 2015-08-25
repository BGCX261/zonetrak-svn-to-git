<?php

/**

CREATE TABLE IF NOT EXISTS `zones` (
  `ID` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` int(20) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `DELETED_ON` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `zone_waypoints` (
  `ID` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `ZONE_ID` int(20) unsigned NOT NULL,
  `LATITUDE` decimal(18,12) NOT NULL,
  `LONGITUDE` decimal(18,12) NOT NULL,
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`ZONE_ID`) REFERENCES zones(ID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

*/

class Model
{
    public static function createZone($type)
    {
        $db = new MySQL;

        $sql = 'INSERT INTO zones ';
        $sql .= '(`ID`, `TYPE`, `CREATED_ON`, `DELETED_ON`) ';
        $sql .= 'VALUES (\'\', %s, NOW(), \'0000-00-00 00:00:00\');';

        if ($db->query($sql, $type))
        {
            return mysql_insert_id();
        }
        else
        {
            return 0;
        }
    }

    public static function getZones()
    {
        $db = new MySQL;

        $sql = 'SELECT * FROM `zones`;';

        return $db->fetchData($sql);
    }

    public static function deleteZone($zoneID)
    {
        $db = new MySQL;

        $sql = 'UPDATE `zones` SET `DELETED_ON`=NOW() WHERE ID=%s;';

        return $db->query($sql, $zoneID);
    }

    public static function createWaypoint($zoneID, $lat, $lon)
    {
        $db = new MySQL;

        $sql = 'INSERT INTO zone_waypoints ';
        $sql .= '(`ID`, `ZONE_ID`, `LATITUDE`, `LONGITUDE`) ';
        $sql .= 'VALUES (\'\', %s, %s, %s);';

        return $db->query($sql, $zoneID, $lat, $lon);
    }

    public static function getWaypoints($zoneID)
    {
        $db = new MySQL;

        $sql = 'SELECT * FROM `zone_waypoints` WHERE ZONE_ID=%s;';

        return $db->fetchData($sql, $zoneID);
    }
    
    public static function detachEntity($identifier)
    {
        $db = new MySQL;
        
        $sql = 'UPDATE entities SET DETACHED=1 WHERE `IDENTIFIER`=%s';
        
        return $db->query($sql, $identifier);
    }

    public static function createEntity($identifier, $friendlyname, $lat, $lon)
    {
        $db = new MySQL;

        $sql = 'INSERT INTO entities ';
        $sql .= '(`ID`, `IDENTIFIER`, `FRIENDLYNAME`, `LATITUDE`, `LONGITUDE`, `DATETIME`, `DETACHED`) ';
        $sql .= 'VALUES (\'\', %s, %s, %s, %s, NOW(), 0);';

        return $db->query($sql, $identifier, $friendlyname, $lat, $lon);
    }

    public static function getRecentEntities()
    {
        $db = new MySQL;

//        $sql = 'SELECT *, MAX(`DATETIME`) FROM `entities` ';
//        $sql .= 'GROUP BY `IDENTIFIER` ORDER BY `IDENTIFIER` DESC;';

        $sql = 'SELECT * FROM entities WHERE DETACHED=0';

        return $db->fetchData($sql);
    }

    public static function getEntities()
    {
        $db = new MySQL;

        $sql = 'SELECT * FROM `entities`;';

        return $db->fetchData($sql);
    }
}

?>