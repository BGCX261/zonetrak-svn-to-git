<?php

class REST
{
    public static function sendResponse($status = 200, $body = '', $contentType = 'text/plain')
    {
        header('HTTP/1.1 ' . $status . ' ' . REST::getStatusCodeMessage($status));
        header('Content-type: ' . $contentType);

        if(empty($body))
        {
            $body = REST::getStatusCodeMessage($status);
        }

        switch ($contentType)
        {
            case 'image/png':
                imagepng($body);
                break;

            case 'application/xml':
                echo strval($body);
                break;

            default:
                echo $body;
                break;
        }

        exit;
    }

    public static function getParameters()
    {
        switch ($_SERVER['REQUEST_METHOD'])
        {
            case 'GET':
                return $_GET;

            case 'POST':
                return $_POST;

            case 'PUT':
                parse_str(file_get_contents('php://input'), $params);
                return $params;

            default:
                return $_GET;
        }
    }

    public static function requireRequestMethod($method)
    {
        if ($_SERVER['REQUEST_METHOD'] == $method)
        {
            return;
        }

        REST::sendResponse(405);
    }

    public static function getAccept($accept)
    {
        $acceptedTypes = explode(",", $accept);
        $types = array('application/xml', 'text/x-json', 'text/plain', 'image/png');

        foreach ($acceptedTypes as $acceptedType)
        {
            $typeInfo = explode(";", $acceptedType);

            if (in_array(trim($typeInfo[0]), $types))
            {
                return trim($typeInfo[0]);
            }
        }

        $typeInfo = explode(";", $acceptedTypes[0]);

        return trim($typeInfo[0]);
    }

    public static function getStatusCodeMessage($status)
    {
        $codes = array(
            100 => 'Continue',
            101 => 'Switching Protocols',
            200 => 'OK',
            201 => 'Created',
            202 => 'Accepted',
            203 => 'Non-Authoritative Information',
            204 => 'No Content',
            205 => 'Reset Content',
            206 => 'Partial Content',
            300 => 'Multiple Choices',
            301 => 'Moved Permanently',
            302 => 'Found',
            303 => 'See Other',
            304 => 'Not Modified',
            305 => 'Use Proxy',
            306 => '(Unused)',
            307 => 'Temporary Redirect',
            400 => 'Bad Request',
            401 => 'Unauthorized',
            402 => 'Payment Required',
            403 => 'Forbidden',
            404 => 'Not Found',
            405 => 'Method Not Allowed',
            406 => 'Not Acceptable',
            407 => 'Proxy Authentication Required',
            408 => 'Request Timeout',
            409 => 'Conflict',
            410 => 'Gone',
            411 => 'Length Required',
            412 => 'Precondition Failed',
            413 => 'Request Entity Too Large',
            414 => 'Request-URI Too Long',
            415 => 'Unsupported Media Type',
            416 => 'Requested Range Not Satisfiable',
            417 => 'Expectation Failed',
            500 => 'Internal Server Error',
            501 => 'Not Implemented',
            502 => 'Bad Gateway',
            503 => 'Service Unavailable',
            504 => 'Gateway Timeout',
            505 => 'HTTP Version Not Supported'
        );

        return (isset($codes[$status])) ? $codes[$status] : '';
    }
}

?>