<?php

function selectData($tableName)
{
    include("dbConfig.php");
    $data = [];
    $teller = 0;
    if ($tableName != "" && $tableName != null) {
        $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
        if (mysqli_connect_errno()) {
            $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
        } else {
            $sql = "SELECT * FROM $tableName";
            if (!$con->query($sql)) {
                $data["error"] = "Failed to execute the query: " . $con->error;
            } else {
                $query = $con->query($sql);
                $row = $query->fetch_array(MYSQLI_ASSOC);
                if ($row === null) {
                    $data["error"] = "Er zijn geen rijen gevonden.";
                } else {
                    do{
                        $data[$teller] = $row;
                        $teller++;
                    }while ($row = $query->fetch_array(MYSQLI_ASSOC));
                }
                $query->close();
            }
        }
        $con->close();
    } else {
        $data["error"] = "Could not read tablename (did you passed the tablename?)";
    }
    return json_encode($data);
}

function selectDataById($tableName, $id)
{
    include("dbConfig.php");
    $data = [];
    $teller = 0;
    if ($tableName != "" && $tableName != null && $id != null && $id != "") {
        $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
        if (mysqli_connect_errno()) {
            $data["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
        } else {
            $sql = "SELECT * FROM $tableName WHERE `ID` = ?";
            $query = $con->prepare($sql);
            if ($query === false) {
                $data["error"] = "Failed to prepare the query: " . $con->error;
            } else {
                $bp = $query->bind_param("i", $id);
                if ($bp === false) {
                    $data["error"] = "Failed to bind params: " . $query->error;
                } else {
                    $exec = $query->execute();
                    if ($exec === false) {
                        $data["error"] = "Failed to execute the query: " . $query->error;
                    } else {
                        $meta = $query->result_metadata();
                        while ($field = $meta->fetch_field()) {
                            $params[] = &$row[$field->name];
                        }
                        call_user_func_array(array($query, 'bind_result'), $params);
                        $fetch = $query->fetch();
                        if ($fetch === null) {
                            $data["error"] = "Er zijn geen overeenkomstige rijen gevonden.";
                        } else {
                            do {
                                $data[$teller] = $row;
                                $teller++;
                            }while($query->fetch());
                        }
                    }
                }
                $query->close();
            }
            $con->close();
        }
    } else {
        $data["error"] = "Could not read tablename or id (did you pass both tablename and id?)";
    }
    return json_encode($data);
}

?>