<?php
header('Content-Type: application/json');
include("dbConfig.php");
$data = "";
$array = [];
$teller = 0;
if (!empty ($_POST["tableName"])) {
    $tableName = $_POST["tableName"];
    if (!empty ($_POST["id"])) {
        if (is_numeric($_POST["id"])) {
            $id = $_POST["id"];
            $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
            if (mysqli_connect_errno()) {
                $array["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
            } else {
                $sql = "SELECT * FROM $tableName WHERE `ID` = ?";
                $query = $con->prepare($sql);
                if ($query === false) {
                    $array["error"] = "Failed to prepare the query: " . $con->error;
                } else {
                    $bp = $query->bind_param("i", $id);
                    if ($bp === false) {
                        $array["error"] = "Failed to bind params: " . $query->error;
                    } else {
                        $exec = $query->execute();
                        if ($exec === false) {
                            $array["error"] = "Failed to execute the query: " . $query->error;
                        } else {
                            $meta = $query->result_metadata();
                            while ($field = $meta->fetch_field()) {
                                $params[] = &$row[$field->name];
                            }
                            call_user_func_array(array($query, 'bind_result'), $params);
                            $fetch = $query->fetch();
                            if ($fetch === null) {
                                $array["error"] = "Er zijn geen overeenkomstige rijen gevonden.";
                            } else {
                                do {
                                    $array["result"][$teller] = $row;
                                    $teller++;
                                } while ($query->fetch());
                            }
                        }
                    }
                    $query->close();
                }
                $con->close();
            }
        } else {
            $array["error"] = "ID is not a number";
        }
    } else {
        $con = new mysqli($dbServer, $dbUsername, $dbPassword, $dbDatabase);
        if (mysqli_connect_errno()) {
            $array["error"] = "Failed to connect to MySQL: " . mysqli_connect_error();
        } else {
            $sql = "SELECT * FROM $tableName";
            if (!$con->query($sql)) {
                $array["error"] = "Failed to do the query: " . $con->error;
            } else {
                $query = $con->query($sql);
                $row = $query->fetch_array(MYSQLI_ASSOC);
                if ($row === null) {
                    $array["error"] = "No rows found";
                } else {
                    do {
                        $array["result"][$teller] = $row;
                        $teller++;
                    } while ($row = $query->fetch_array(MYSQLI_ASSOC));
                }
                $query->close();
            }
        }
        $con->close();
    }
} else {
    $array["error"] = "Tablename not found.";
}
print json_encode($array);
//$data = json_encode($array);
?>