<?php

class MySQL
{
    private $link;
    private $counter;

    public function __construct($hostname = null, $username = null, $password = null, $database = null)
    {
        $this->link = mysql_connect($hostname ? $hostname : MYSQL_HOSTNAME, $username ? $username : MYSQL_USERNAME, $password ? $password : MYSQL_PASSWORD);

        if(!$this->link)
        {
            print(mysql_error());
        }

        $db_selected = mysql_select_db($database ? $database : MYSQL_DATABASE, $this->link);

        if(!$db_selected)
        {
            print(mysql_error());
        }

        $this->counter = 0;
    }

    public function __destruct()
    {
        @mysql_close($this->link);
    }

    public function quoteSmart($value)
    {
        if (get_magic_quotes_gpc())
        {
            $value = stripslashes($value);
        }

        if (!is_numeric($value))
        {
            $value = '\'' . mysql_real_escape_string($value, $this->link) . '\'';
        }

        return $value;
    }

    public function query()
    {
        $arg_list = func_get_args();
        $num_args = func_num_args();

        for ($i = 1; $i < $num_args; $i++)
        {
            $arg_list[$i] = $this->quoteSmart($arg_list[$i], $this->link);
        }

        $this->counter++;

        return mysql_query(call_user_func_array('sprintf', $arg_list), $this->link);
    }

    public function fetchData()
    {
        $arg_list = func_get_args();

        $result = call_user_func_array(array($this, 'query'), $arg_list);

        $data = array();

        while ($row = mysql_fetch_assoc($result))
        {
            $data[] = $row;
        }

        return $data;
    }

    public function getQueryCount()
    {
        return $this->counter;
    }
}

?>